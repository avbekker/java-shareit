package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageInMemory implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1L;

    @Override
    public User create(User user) {
        user.setId(id);
        users.put(id++, user);
        return user;
    }

    @Override
    public User update(long id, User user) {
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public Optional<User> getById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User ID = " + id + " not found.");
        }
        return Optional.of(users.get(id));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(long id) {
        users.remove(id);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
