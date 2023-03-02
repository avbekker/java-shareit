package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User owner = userService.getById(userId);
        Item item = itemStorage.create(owner.getId(), fromItemDto(itemDto, userId));
        return toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        Item item = itemStorage.getById(userId, itemId);
        if (item.getOwnerId() != userId) {
            throw new AccessException("User ID = " + userId + " is not owner.");
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return toItemDto(item);
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
