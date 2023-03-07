package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toShortBookingDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemDtoResponse create(long userId, ItemDtoRequest itemDtoRequest) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID = " + userId + " not found."));
        Item item = fromItemDtoRequest(itemDtoRequest, owner);
        item.setOwner(owner);
        Item result = itemRepository.save(item);
        return toItemDtoResponse(result);
    }

    @Override
    @Transactional
    public ItemDtoResponse update(long userId, long itemId, ItemDtoRequest itemDtoRequest) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item ID = " + itemId + " not found."));
        if (item.getOwner().getId() != userId) {
            throw new AccessException("User ID = " + userId + " is not owner.");
        }
        if (itemDtoRequest.getDescription() != null && !itemDtoRequest.getDescription().isBlank()) {
            item.setDescription(itemDtoRequest.getDescription());
        }
        if (itemDtoRequest.getName() != null && !itemDtoRequest.getName().isBlank()) {
            item.setName(itemDtoRequest.getName());
        }
        if (itemDtoRequest.getAvailable() != null) {
            item.setAvailable(itemDtoRequest.getAvailable());
        }
        return toItemDtoResponse(item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoResponse getById(long userId, long itemId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID = " + userId + " not found."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item ID = " + itemId + " not found."));
        ItemDtoResponse result = toItemDtoResponse(item);
        List<CommentDto> comments = commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        Booking lastBooking = bookingRepository.findByItemIdAndEndIsBefore(item.getId(), LocalDateTime.now())
                .stream().max(Comparator.comparing(Booking::getEnd))
                .orElse(null);
        Booking nextBooking = bookingRepository.findByItemIdAndStartIsAfter(item.getId(), LocalDateTime.now())
                .stream().min(Comparator.comparing(Booking::getStart))
                .filter(booking -> booking.getStatus().equals(BookingStatus.APPROVED))
                .orElse(null);
        result.setComments(comments);
        if (userId == item.getOwner().getId()) {
            result.setLastBooking(lastBooking == null ? null : toShortBookingDto(lastBooking));
            result.setNextBooking(nextBooking == null ? null : toShortBookingDto(nextBooking));
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoResponse> getAll(long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return items.stream().map(item -> getById(userId, item.getId())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(long userId, long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoResponse> search(String text) {
        return toItemDtoList(itemRepository.search(text.toLowerCase()));
    }
}
