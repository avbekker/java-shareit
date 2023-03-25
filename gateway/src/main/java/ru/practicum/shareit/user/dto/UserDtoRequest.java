package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.exception.OnCreate;
import ru.practicum.shareit.exception.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {

    @NotBlank(groups = {OnCreate.class})
    private String name;

    @NotBlank(groups = {OnCreate.class})
    @Email(groups = {OnCreate.class, OnUpdate.class})
    private String email;
}
