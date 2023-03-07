package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.item.mapper.CommentMapper.toComment;
import static ru.practicum.shareit.item.mapper.CommentMapper.toCommentDto;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;


    @Override
    public CommentDto create(long userId, long itemId, CommentDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID = " + userId + " not found."));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item ID = " + itemId + " not found."));
        List<Booking> bookings = bookingRepository.findByBookerIdAndItemIdAndEndIsBefore(userId, itemId, LocalDateTime.now());
        if (bookings.stream().noneMatch(booking -> booking.getStatus().equals(BookingStatus.APPROVED))) {
            throw new BadRequestException("User ID = " + userId + " did not take Item ID = " + itemId + ".");
        }
        Comment comment = toComment(commentDto);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(comment));
    }
}
