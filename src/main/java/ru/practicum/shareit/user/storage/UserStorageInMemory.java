package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageInMemory implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 1L;

    @Override
    public User create(User user) {
        user.setId(id);
        users.put(id++, user);
        return user;
    }

    @Override
    public User getById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User ID = " + id + " not found.");
        }
        return users.get(id);
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
