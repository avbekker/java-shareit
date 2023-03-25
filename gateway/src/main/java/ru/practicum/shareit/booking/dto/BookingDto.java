package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.EndAfterStartValidation;
import ru.practicum.shareit.validator.OnCreate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EndAfterStartValidation(groups = {OnCreate.class})
public class BookingDto {

    private long itemId;

    @FutureOrPresent(groups = {OnCreate.class})
    private LocalDateTime start;

    @Future(groups = {OnCreate.class})
    private LocalDateTime end;
}
