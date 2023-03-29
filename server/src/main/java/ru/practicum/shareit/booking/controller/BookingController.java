package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookingDtoResponse create(@RequestBody BookingDtoRequest bookingDtoRequest,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("SERVER: Received POST request for new Booking for Item ID = {}", bookingDtoRequest.getItemId());
        return bookingService.create(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@PathVariable long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam boolean approved) {
        log.info("SERVER: Received PATCH request for Booking ID = {}", bookingId);
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse findById(@PathVariable long bookingId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("SERVER: Received GET request for Booking ID = {}", bookingId);
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> findByBooker(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "from", defaultValue = "0") int from) {
        log.info("SERVER: Received GET request for Bookings of Booker ID = {}", userId);
        return bookingService.findByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> findByOwner(
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "from", defaultValue = "0") int from) {
        log.info("SERVER: Received GET request for Bookings of Owner ID = {}", userId);
        return bookingService.findByOwner(userId, state, from, size);
    }
}
