package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User update(long id, User user);
    User getById(long id);
    List<User> getAll();
    void deleteById(long id);
    void deleteAll();
}
