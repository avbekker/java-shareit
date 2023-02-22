package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemStorageInMemory implements ItemStorage {

    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item create(long userId, Item item) {
        item.setId(++id);
        items.put(id, item);
        userItemIndex.computeIfAbsent(item.getOwnerId(), ownerId -> new ArrayList<>()).add(item);
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
        return userItemIndex.get(userId);
    }

    @Override
    public void deleteById(long userId, long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Item with ID = " + itemId + " not exist.");
        }
        userItemIndex.values().forEach(items -> items.removeIf(item -> item.getId() == itemId));
        items.remove(itemId);
    }

    @Override
    public void deleteAll(long userId) {
        userItemIndex.remove(userId);
        items.values().removeIf(item -> item.getOwnerId() == userId);
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
