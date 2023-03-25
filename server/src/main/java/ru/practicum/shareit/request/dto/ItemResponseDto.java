package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.exception.OnCreate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private long id;

    @NotBlank(groups = {OnCreate.class})
    private String description;

    private LocalDateTime created;

    private List<ItemDtoResponse> items;
}
