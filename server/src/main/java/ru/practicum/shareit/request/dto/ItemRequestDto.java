package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.exception.OnCreate;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private long id;

    @NotBlank(groups = {OnCreate.class})
    private String description;
}
