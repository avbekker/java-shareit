package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerAndEndIsBefore(User booker, LocalDateTime end, Sort sort);

    List<Booking> findByBookerAndStartIsAfter(User booker, LocalDateTime start, Sort sort);

    List<Booking> findByBookerAndStartIsBeforeAndEndIsAfter(
            User booker, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBookerAndStatusIs(User booker, BookingStatus status, Sort sort);

    List<Booking> findByBooker(User booker, Sort sort);

    List<Booking> findByItemOwnerId(long ownerId, Sort sort);

    List<Booking> findByItemIdInAndStartIsAfter(List<Long> ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemIdInAndEndIsBefore(List<Long> ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemIdInAndStatusIs(List<Long> ownerId, BookingStatus status, Sort sort);

    List<Booking> findByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, LocalDateTime end);

    List<Booking> findByItemIdInAndStartIsBeforeAndEndIsAfter(List<Long> ownerItems, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByItemIdAndStartIsAfter(long itemId, LocalDateTime start);

    List<Booking> findByItemIdAndEndIsBefore(long itemId, LocalDateTime start);

    List<Booking> findAllByItemInAndStatus(List<Item> items, BookingStatus status, Sort sort);
}
