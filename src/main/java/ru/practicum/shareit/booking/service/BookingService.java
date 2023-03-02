package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

public interface BookingService {

    BookingDtoResponse create(BookingDtoRequest bookingDto, long userId);

    BookingDtoResponse approve(long bookingId, long userId, boolean approved);

    BookingDtoResponse findById(long bookingId, long userId);

    List<BookingDtoResponse> findByBooker(long userId, BookingState bookingState);

    List<BookingDtoResponse> findByOwner(long userId, BookingState bookingState);
}
