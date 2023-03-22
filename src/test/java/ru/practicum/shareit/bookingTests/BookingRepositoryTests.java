package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Rollback(value = false)
public class BookingRepositoryTests {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User booker;
    private User owner;
    private Booking booking;
    private Item item;

    @BeforeEach
    void prepareBeforeEach() {
        booker = userRepository.save(User.builder().name("bookerOne").email("bookerOne@bookerOne.ru").build());
        owner = userRepository.save(User.builder().name("owner").email("owner@owner.ru").build());
        item = itemRepository.save(Item.builder().name("item").description("description").available(true).owner(owner).build());
        booking = bookingRepository.save(Booking.builder().item(item).booker(booker).start(LocalDateTime.now())
                .end(LocalDateTime.now()).status(BookingStatus.APPROVED).build());
    }

    @AfterEach
    void cleanAfterEach() {
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findByBookerAndEndIsBeforeOrderByStartDesc() {
        booking.setEnd(booking.getEnd().minusHours(1));
        bookingRepository.save(booking);
        List<Booking> result = bookingRepository.findByBookerAndEndIsBefore(
                booker, LocalDateTime.now(), Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerAndStartIsAfterOrderByStartDesc() {
        booking.setStart(booking.getStart().plusHours(1));
        bookingRepository.save(booking);
        List<Booking> result = bookingRepository.findByBookerAndStartIsAfter(
                booker, LocalDateTime.now(), Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        booking.setStart(booking.getStart().minusHours(1));
        booking.setEnd(booking.getEnd().plusHours(2));
        bookingRepository.save(booking);
        List<Booking> result = bookingRepository.findByBookerAndStartIsBeforeAndEndIsAfter(
                booker, LocalDateTime.now(), LocalDateTime.now(), Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerAndStatusIsOrderByStartDesc() {
        List<Booking> result = bookingRepository.findByBookerAndStatusIs(
                booker, BookingStatus.APPROVED, Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByBookerOrderByStartDesc() {
        List<Booking> result = bookingRepository.findByBooker(booker, Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc() {
        List<Booking> result = bookingRepository.findByItemOwnerId(
                owner.getId(), Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByItemOwnerIdAndStartIsAfterOrderByStartDesc() {
        booking.setStart(booking.getStart().plusHours(1));
        bookingRepository.save(booking);
        List<Booking> result = bookingRepository.findByItemOwnerIdAndStartIsAfter(
                owner.getId(), LocalDateTime.now(), Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByItemOwnerIdAndEndIsBeforeOrderByStartDesc() {
        booking.setEnd(booking.getEnd().minusHours(1));
        bookingRepository.save(booking);
        List<Booking> result = bookingRepository.findByItemOwnerIdAndEndIsBefore(
                owner.getId(), LocalDateTime.now(), Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByItemOwnerIdAndStatusIsOrderByStartDesc() {
        List<Booking> result = bookingRepository.findByItemOwnerIdAndStatusIs(
                owner.getId(), BookingStatus.APPROVED, Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs() {
        booking.setEnd(booking.getEnd().minusHours(1));
        bookingRepository.save(booking);
        Booking result = bookingRepository.findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(
                booker.getId(), item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        assertNotNull(result);
        assertEquals(booker.getName(), result.getBooker().getName());
    }

    @Test
    void findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc() {
        booking.setStart(booking.getStart().minusHours(1));
        booking.setEnd(booking.getEnd().plusHours(1));
        bookingRepository.save(booking);
        List<Booking> result = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                owner.getId(), LocalDateTime.now(), LocalDateTime.now(), Pageable.ofSize(1));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findFirstByItemIdAndStartGreaterThanEqualAndStatusIsOrderByStartAsc() {
        booking.setStart(booking.getStart().plusHours(1));
        bookingRepository.save(booking);
        Booking result = bookingRepository.findFirstByItemIdAndStartGreaterThanEqualAndStatusIsOrderByStartAsc(
                item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        assertNotNull(result);
        assertEquals(item.getName(), result.getItem().getName());
    }

    @Test
    void findFirstByItemIdAndStartLessThanEqualOrderByStartDesc() {
        booking.setStart(booking.getStart().minusHours(1));
        bookingRepository.save(booking);
        Booking result = bookingRepository.findFirstByItemIdAndStartLessThanEqualOrderByStartDesc(
                item.getId(), LocalDateTime.now());
        assertNotNull(result);
        assertEquals(item.getName(), result.getItem().getName());
    }

    @Test
    void findAllByItemInAndStatus() {
        List<Booking> result = bookingRepository.findAllByItemInAndStatus(
                List.of(item), BookingStatus.APPROVED, Sort.by(Sort.Direction.DESC, "start"));
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
