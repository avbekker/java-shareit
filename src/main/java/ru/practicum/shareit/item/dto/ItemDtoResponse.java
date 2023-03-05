package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDtoResponse {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    private Booking lastBooking;

    private Booking nextBooking;

    private List<CommentDto> comments;
}
