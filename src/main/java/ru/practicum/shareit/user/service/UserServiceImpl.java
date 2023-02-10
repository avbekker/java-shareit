package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserStorage userStorage;

    @Override
    public User create(User user) {
        if (userStorage.getAll().contains(user)) {
            throw new AlreadyExistException("User already exist.");
        }
        return userStorage.create(user);
    }

    @Override
    public User update(long id, User user) {
        if (userStorage.getById(id).isEmpty()) {
            throw new NotFoundException("User ID = " + id + " not found.");
        }
        return userStorage.update(id, user);
    }

    @Override
    public User getById(long id) {
        return userStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("User ID = " + id + " not found."));
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void deleteById(long id) {
        if (userStorage.getById(id).isEmpty()) {
            throw new NotFoundException("User ID = " + id + " not found.");
        }
        userStorage.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userStorage.deleteAll();
    }
}
