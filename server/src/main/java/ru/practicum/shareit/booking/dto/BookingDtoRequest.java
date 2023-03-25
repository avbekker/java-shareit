package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookingDtoRequest {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private long itemId;
}
