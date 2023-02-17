package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);
    ItemDto update(long userId, long itemId, ItemDto itemDto);
    ItemDto getById(long userId, long itemId);
    List<ItemDto> getAll(long userId);
    void deleteById(long userId, long itemId);
    void deleteAll(long userId);
    List<ItemDto> search(String text);
}
