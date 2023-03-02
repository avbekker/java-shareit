package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {

    public static BookingDtoResponse toBookingDtoResponse(Booking booking) {
        return BookingDtoResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(Item.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .booker(User.builder()
                        .id(booking.getBooker().getId())
                        .name(booking.getBooker().getName())
                        .build())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDtoRequest bookingDtoRequest) {
        return Booking.builder()
                .start(bookingDtoRequest.getStart())
                .end(bookingDtoRequest.getEnd())
                .status(BookingStatus.WAITING)
                .build();
    }
}
