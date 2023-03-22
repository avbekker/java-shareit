package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.BookingStateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookingServiceImplTests {

    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final BookingService service = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

    @Test
    void create() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).item(item).booker(booker).status(BookingStatus.APPROVED).build();
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder().itemId(item.getId()).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingDtoResponse result = service.create(bookingDtoRequest, booker.getId());
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
    }

    @Test
    void createFail() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).item(item).booker(booker).status(BookingStatus.APPROVED).build();
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder().itemId(item.getId()).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        assertThrows(NotFoundException.class, () -> service.create(bookingDtoRequest, owner.getId()));
    }

    @Test
    void approve() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.WAITING).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        BookingDtoResponse result = service.approve(booking.getId(), owner.getId(), true);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void approveBad() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        assertThrows(BadRequestException.class, () -> service.approve(booking.getId(), owner.getId(), true));
    }

    @Test
    void approveRejected() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.WAITING).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        BookingDtoResponse result = service.approve(booking.getId(), owner.getId(), false);
        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void findById() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        BookingDtoResponse result = service.findById(booking.getId(), booker.getId());
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getItem().getId(), result.getItem().getId());
    }

    @Test
    void findByBookerPast() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByBookerAndEndIsBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByBooker(booker.getId(), "PAST", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerFuture() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByBookerAndStartIsAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByBooker(booker.getId(), "FUTURE", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerCurrent() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(any(User.class),
                any(LocalDateTime.class), any(LocalDateTime.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByBooker(booker.getId(), "CURRENT", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerWaiting() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByBookerAndStatusIsOrderByStartDesc(any(User.class),
                any(BookingStatus.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByBooker(booker.getId(), "WAITING", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerDefault() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByBookerOrderByStartDesc(any(User.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByBooker(booker.getId(), "ALL", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByOwnerPast() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByOwner(booker.getId(), "PAST", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByOwnerFuture() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByOwner(booker.getId(), "FUTURE", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByOwnerCurrent() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByOwner(booker.getId(), "CURRENT", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByOwnerReject() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(anyLong(), any(BookingStatus.class), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByOwner(booker.getId(), "REJECTED", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByOwnerDefault() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> result = service.findByOwner(booker.getId(), "ALL", 0, 1);
        assertEquals(1, result.size());
    }

    @Test
    void findByOwnerFail() {
        User booker = User.builder().id(1L).name("user").email("user@user.ru").build();
        User owner = User.builder().id(2L).name("owner").email("owner@owner.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        Booking booking = Booking.builder().id(1L).booker(booker).item(item).status(BookingStatus.APPROVED).build();
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        assertThrows(BookingStateException.class, () -> service.findByOwner(booker.getId(), "fail", 0, 1));
    }
}
