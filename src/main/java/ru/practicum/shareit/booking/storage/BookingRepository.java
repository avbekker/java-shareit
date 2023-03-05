package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerAndEndIsBefore(User booker, LocalDateTime end, Sort sort);

    List<Booking> findByBookerAndStartIsAfter(User booker, LocalDateTime start, Sort sort);

    List<Booking> findByBookerAndStartIsBeforeAndEndIsAfter(
            User booker, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBookerAndStatusIs(User booker, BookingStatus status, Sort sort);

    List<Booking> findByBooker(User booker, Sort sort);

    List<Booking> findByItemOwnerId(long ownerId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(long ownerId, LocalDateTime start, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStatusIs(long ownerId, BookingStatus status, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
            long ownerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByBookerIdAndItemIdAndEndIsBefore(Long bookerId, Long itemId, LocalDateTime end);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :userId AND b.status = 'APPROVED' AND b.end < :now " +
            "ORDER BY b.start DESC")
    Booking findLastBooking(LocalDateTime now, Long userId, Long itemId);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :userId AND b.status = 'APPROVED' AND b.start > :now " +
            "ORDER BY b.start")
    Booking findNextBooking(LocalDateTime now, Long userId, Long itemId);
}
