package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.util.List;

public interface ItemService {
    ItemDtoResponse create(long userId, ItemDtoResponse itemDtoResponse);

    ItemDtoResponse update(long userId, long itemId, ItemDtoResponse itemDtoResponse);

    ItemDtoResponse getById(long userId, long itemId);

    List<ItemDtoResponse> getAll(long userId);

    void deleteById(long userId, long itemId);

    List<ItemDtoResponse> search(String text);
}
