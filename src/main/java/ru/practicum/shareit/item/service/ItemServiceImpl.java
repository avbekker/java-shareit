package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemStorage itemStorage;

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        itemStorage.create(userId, fromItemDto(itemDto, userId, null));
        return itemDto;
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        itemStorage.update(userId, itemId, fromItemDto(itemDto, userId, null));
        return itemDto;
    }

    @Override
    public ItemDto getById(long userId, long itemId) {
        return toItemDto(itemStorage.getById(userId, itemId));
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return toItemDtoList(itemStorage.getAll(userId));
    }

    @Override
    public void deleteById(long userId, long itemId) {
        itemStorage.deleteById(userId, itemId);
    }

    @Override
    public void deleteAll(long userId) {
        itemStorage.deleteAll(userId);
    }

    @Override
    public List<ItemDto> search(String text) {
        return toItemDtoList(itemStorage.search(text.toLowerCase()));
    }
}
