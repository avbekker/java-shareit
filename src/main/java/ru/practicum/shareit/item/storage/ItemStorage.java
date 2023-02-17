package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item create(long userId, Item item);
    Item update(long userId, long itemId, Item item);
    Item getById(long userId, long itemId);
    List<Item> getAll(long userId);
    void deleteById(long userId, long itemId);
    void deleteAll(long userId);
    List<Item> search(String text);
}
