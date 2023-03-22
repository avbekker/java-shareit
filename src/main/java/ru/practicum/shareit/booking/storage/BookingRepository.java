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

    List<Booking> findByBookerAndEndIsBefore(User booker, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerAndStartIsAfter(User booker, LocalDateTime start, Pageable pageable);

    List<Booking> findByBookerAndStartIsBeforeAndEndIsAfter(
            User booker, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerAndStatusIs(User booker, BookingStatus status, Pageable pageable);

    List<Booking> findByBooker(User booker, Pageable pageable);

    List<Booking> findByItemOwnerId(long ownerId, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartIsAfter(long ownerId, LocalDateTime start, Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime end, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusIs(long ownerId, BookingStatus status, Pageable pageable);

    Booking findFirstByBookerIdAndItemIdAndEndIsBeforeAndStatusIs(Long bookerId, Long itemId, LocalDateTime end, BookingStatus status);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(long ownerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

   Booking findFirstByItemIdAndStartGreaterThanEqualAndStatusIsOrderByStartAsc(long itemId, LocalDateTime start, BookingStatus status);

    Booking findFirstByItemIdAndStartLessThanEqualOrderByStartDesc(long itemId, LocalDateTime start);

    List<Booking> findAllByItemInAndStatus(List<Item> items, BookingStatus status, Sort sort);
}
