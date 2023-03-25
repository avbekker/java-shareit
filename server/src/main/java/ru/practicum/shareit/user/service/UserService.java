package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(long id, UserDto userDto);

    User getById(long id);

    List<User> getAll();

    void deleteById(long id);

    void deleteAll();
}
