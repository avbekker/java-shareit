package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDtoResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoResponse create(BookingDtoRequest bookingDtoRequest, long userId) {
        Item item = itemRepository.findByIdAndOwnerId(bookingDtoRequest.getItemId(), userId)
                .orElseThrow(() -> new NotFoundException("Item with ID = " + bookingDtoRequest.getItemId() + " not exist."));
        if (!item.getAvailable()) {
            throw new AccessException("Item with ID = " + item.getId() + " is not available.");
        }
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + " not exist."));
        Booking booking = toBooking(bookingDtoRequest);
        booking.setBooker(booker);
        booking.setItem(item);
        booking = bookingRepository.save(booking);
        return toBookingDtoResponse(booking);
    }

    @Override
    public BookingDtoResponse approve(long bookingId, long userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with ID = " + bookingId + " not exist."));
        if (userId != booking.getItem().getOwnerId()) {
            throw new AccessException("User ID = " + userId + " is not owner.");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BadRequestException("Status is already approved.");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return toBookingDtoResponse(booking);
    }

    @Override
    public BookingDtoResponse findById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with ID = " + bookingId + " not found."));
        if (userId != booking.getBooker().getId() || userId != booking.getItem().getOwnerId()) {
            throw new AccessException("User with ID = " + userId + " is not booker or owner and have no access.");
        }
        return toBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDtoResponse> findByBooker(long userId, BookingState bookingState) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + " not exist."));
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (bookingState) {
            case PAST:
                bookings = bookingRepository.findByBookerAndEndIsBefore
                        (booker, now, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerAndStartIsAfter
                        (booker, now, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerAndStartIsBeforeAndEndIsAfter
                        (booker, now, now, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerAndStatusIs
                        (booker, BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatusIs
                        (booker, BookingStatus.REJECTED, sort);
                break;
            default:
                bookings = bookingRepository
                        .findByBooker(booker, sort);
                break;
        }
        return bookings.stream().map(BookingMapper::toBookingDtoResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoResponse> findByOwner(long userId, BookingState bookingState) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + " not exist."));
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (bookingState) {
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore
                        (owner.getId(), now, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfter
                        (owner.getId(), now, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter
                        (owner.getId(), now, now, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatusIs
                        (owner.getId(), BookingStatus.WAITING, sort);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatusIs
                        (owner.getId(), BookingStatus.REJECTED, sort);
                break;
            default:
                bookings = bookingRepository.findByItemOwnerId(owner.getId(), sort);
                break;
        }
        return bookings.stream().map(BookingMapper::toBookingDtoResponse).collect(Collectors.toList());
    }
}
