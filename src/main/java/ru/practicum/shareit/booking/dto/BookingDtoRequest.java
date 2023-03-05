package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.OnCreate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDtoRequest {

    private long id;

    @NotNull(groups = {OnCreate.class})
    @FutureOrPresent(groups = {OnCreate.class})
    private LocalDateTime start;

    @NotNull(groups = {OnCreate.class})
    @Future(groups = {OnCreate.class})
    private LocalDateTime end;

    private long itemId;
}