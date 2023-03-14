package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerAndStartIsAfterOrderByStartDesc(User booker, LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerAndStatusIsOrderByStartDesc(User booker, BookingStatus status, Pageable pageable);

    List<Booking> findByBookerOrderByStartDesc(User booker, Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long ownerId, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(long ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusIsOrderByStartDesc(long ownerId, BookingStatus status, Pageable pageable);

    Booking findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(Long bookerId, Long itemId, LocalDateTime end, BookingStatus status);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

   Booking findFirstByItemIdAndStartGreaterThanEqualAndStatusIsOrderByStartAsc(long itemId, LocalDateTime start, BookingStatus status);

    Booking findFirstByItemIdAndStartLessThanEqualOrderByStartDesc(long itemId, LocalDateTime start);

    List<Booking> findAllByItemInAndStatus(List<Item> items, BookingStatus status, Sort sort);
}
