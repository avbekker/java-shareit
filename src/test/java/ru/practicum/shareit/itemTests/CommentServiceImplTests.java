package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentServiceImplTests {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final CommentService service = new CommentServiceImpl(userRepository, itemRepository, commentRepository, bookingRepository);

    @Test
    void create() {
        CommentDto commentDto = CommentDto.builder().text("text").build();
        User author = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(author).build();
        Booking booking = Booking.builder().status(BookingStatus.APPROVED).build();
        Comment comment = Comment.builder().id(1L).item(item).author(author).text("text").created(LocalDateTime.now()).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(), anyLong(),
                any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(booking);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        CommentDto result = service.create(author.getId(), item.getId(), commentDto);
        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getText(), result.getText());
    }

    @Test
    void createFail() {
        CommentDto commentDto = CommentDto.builder().text("text").build();
        User author = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(author).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(), anyLong(),
                any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(null);
        assertThrows(BadRequestException.class, () -> service.create(author.getId(), item.getId(), commentDto));
    }

    @Test
    void createFailNotFoundUser() {
        CommentDto commentDto = CommentDto.builder().text("text").build();
        User author = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(author).build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(), anyLong(),
                any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.create(author.getId(), item.getId(), commentDto));
    }

    @Test
    void createFailNotFoundItem() {
        CommentDto commentDto = CommentDto.builder().text("text").build();
        User author = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(author).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(anyLong(), anyLong(),
                any(LocalDateTime.class), any(BookingStatus.class))).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.create(author.getId(), item.getId(), commentDto));
    }
}
