package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
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
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoResponse create(BookingDtoRequest bookingDtoRequest, long userId) {
        Booking booking = toBooking(bookingDtoRequest);
        Item item = itemRepository.findById(bookingDtoRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Item with ID = " + bookingDtoRequest.getItemId() + " not exist."));
        if (!item.getAvailable()) {
            throw new BadRequestException("Item with ID = " + item.getId() + " is not available.");
        }
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + " not exist."));
        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("You can not book your item.");
        }
        booking.setBooker(booker);
        booking.setItem(item);
        booking = bookingRepository.save(booking);
        return toBookingDtoResponse(booking);
    }

    @Override
    @Transactional
    public BookingDtoResponse approve(long bookingId, long userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with ID = " + bookingId + " not exist."));
        if (userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("User ID = " + userId + " is not owner.");
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
    @Transactional(readOnly = true)
    public BookingDtoResponse findById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with ID = " + bookingId + " not found."));
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("User with ID = " + userId + " is not booker or owner and have no access.");
        }
        return toBookingDtoResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findByBooker(long userId, String state, int from, int size) {
        BookingState bookingState = BookingState.isStateExist(state);
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + " not exist."));
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        switch (bookingState) {
            case PAST:
                bookings = bookingRepository.findByBookerAndEndIsBefore(booker, now, pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerAndStartIsAfter(booker, now, pageRequest);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerAndStartIsBeforeAndEndIsAfter(booker, now, now, pageRequest);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerAndStatusIs(booker, BookingStatus.WAITING, pageRequest);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatusIs(booker, BookingStatus.REJECTED, pageRequest);
                break;
            default:
                bookings = bookingRepository.findByBooker(booker, pageRequest);
                break;
        }
        return bookings.stream().map(BookingMapper::toBookingDtoResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDtoResponse> findByOwner(long userId, String state, int from, int size) {
        BookingState bookingState = BookingState.isStateExist(state);
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with ID = " + userId + " not exist."));
        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        switch (bookingState) {
            case PAST:
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, now, pageRequest);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, now, pageRequest);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, now, now, pageRequest);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerIdAndStatusIs(userId, BookingStatus.WAITING, pageRequest);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerIdAndStatusIs(userId, BookingStatus.REJECTED, pageRequest);
                break;
            default:
                bookings = bookingRepository.findByItemOwnerId(owner.getId(), pageRequest);
                break;
        }
        return bookings.stream().map(BookingMapper::toBookingDtoResponse).collect(Collectors.toList());
    }
}
