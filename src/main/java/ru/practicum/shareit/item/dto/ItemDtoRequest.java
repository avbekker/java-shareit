package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.OnCreate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDtoRequest {

    private long id;

    @NotBlank(groups = OnCreate.class)
    @Size(max = 100)
    private String name;

    @NotBlank(groups = OnCreate.class)
    @Size(max = 255)
    private String description;

    @NotNull(groups = OnCreate.class)
    private Boolean available;
}
