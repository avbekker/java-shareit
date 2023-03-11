package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.exception.EndAfterStartValidation;
import ru.practicum.shareit.exception.OnCreate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@EndAfterStartValidation(groups = {OnCreate.class})
public class BookingDtoRequest {

    private long id;

    @NotNull(groups = OnCreate.class)
    @FutureOrPresent(groups = OnCreate.class)
    private LocalDateTime start;

    @NotNull(groups = OnCreate.class)
    @Future(groups = OnCreate.class)
    private LocalDateTime end;

    private long itemId;
}
