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
    private final Map<Long, Map<Long, Item>> items = new HashMap<>();
    private long id = 1L;

    @Override
    public Item create(long userId, Item item) {
        Map<Long, Item> itemsOfUser = new HashMap<>();
        if (!items.containsKey(userId)) {
            items.put(userId, itemsOfUser);
        } else {
            itemsOfUser = items.get(userId);
            if (itemsOfUser.containsValue(item)) {
                throw new AlreadyExistException("Item already exist.");
            }
        }
        item.setId(id);
        itemsOfUser.put(id++, item);
        return item;
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        if (!items.containsKey(userId) || items.get(userId).isEmpty()) {
            throw new NotFoundException("User with ID = " + userId + " have no items.");
        }
        item.setId(itemId);
        items.get(userId).put(itemId, item);
        return item;
    }

    @Override
    public Item getById(long userId, long itemId) {
        if (!items.containsKey(userId) || !items.get(userId).containsKey(itemId)) {
            throw new NotFoundException("Item with ID = " + itemId + " not exist.");
        }
        return items.get(userId).get(itemId);
    }

    @Override
    public List<Item> getAll(long userId) {
        return new ArrayList<>(items.get(userId).values());
    }

    @Override
    public void deleteById(long userId, long itemId) {
        if (!items.containsKey(userId) || !items.get(userId).containsKey(itemId) || items.get(userId).isEmpty()) {
            throw new NotFoundException("Item with ID = " + itemId + " not exist.");
        }
        items.get(userId).remove(itemId);
    }

    @Override
    public void deleteAll(long userId) {
        items.remove(userId);
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemList = new ArrayList<>();
        items.values().forEach(map -> itemList.addAll(map.values()));
        return itemList.stream()
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }
}
