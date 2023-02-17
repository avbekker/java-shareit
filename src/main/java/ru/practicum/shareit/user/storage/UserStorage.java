package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);
    User update(long id, User user);
    Optional<User> getById(long id);
    List<User> getAll();
    void deleteById(long id);
    void deleteAll();
}
