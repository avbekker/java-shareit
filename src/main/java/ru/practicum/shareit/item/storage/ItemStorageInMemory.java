package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemStorageInMemory implements ItemStorage{
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item create(long userId, Item item) {
        if (items.containsValue(item)) {
            throw new AlreadyExistException("Item already exist.");
        }
        item.setId(++id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Item with ID = " + itemId + " not exist.");
        }
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item getById(long userId, long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Item with ID = " + itemId + " not exist.");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getAll(long userId) {
        return items.values().stream().filter(item -> item.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public void deleteById(long userId, long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Item with ID = " + itemId + " not exist.");
        }
        items.remove(itemId);
    }

    @Override
    public void deleteAll(long userId) {
        items.remove(userId);
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }
}
