package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.validator.OnCreate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {

    private final BookingClient client;

    @PostMapping()
    public ResponseEntity<Object> create(@Validated(OnCreate.class) @RequestBody BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingClient: Received POST request for new Booking for Item ID = {}", bookingDto.getItemId());
        return client.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam boolean approved) {
        log.info("BookingClient: Received PATCH request for Booking ID = {}", bookingId);
        return client.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@PathVariable long bookingId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingClient: Received GET request for Booking ID = {}", bookingId);
        return client.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findByBooker(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from) {
        log.info("BookingClient: Received GET request for Bookings of Booker ID = {}", userId);
        BookingState bookingState = BookingState.isStateExist(state);
        return client.findByBooker(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findByOwner(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from) {
        log.info("BookingClient: Received GET request for Bookings of Owner ID = {}", userId);
        BookingState bookingState = BookingState.isStateExist(state);
        return client.findByOwner(userId, bookingState, from, size);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteById(@PathVariable long bookingId) {
        log.info("BookingClient: Received DELETE request for Booking ID = {}", bookingId);
        client.deleteBooking(bookingId);
    }
}
