package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validator.OnCreate;
import ru.practicum.shareit.validator.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {

    @NotBlank(groups = {OnCreate.class})
    @Size(max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotEmpty(groups = {OnCreate.class})
    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 255, groups = {OnCreate.class, OnUpdate.class})
    private String email;
}
