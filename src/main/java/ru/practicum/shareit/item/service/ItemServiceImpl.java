package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        User owner = userService.getById(userId);
        if (itemDto.getAvailable() == null ||
                itemDto.getDescription() == null ||
                itemDto.getDescription().isEmpty() ||
                itemDto.getName() == null ||
                itemDto.getName().isEmpty()) {
            throw new BadRequestException("Bad request, please check data and try again.");
        }
        Item item = itemStorage.create(owner.getId(), fromItemDto(itemDto, userId, null));
        return toItemDto(item);
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        User owner = userService.getById(userId);
        Item item = itemStorage.getById(userId, itemId);
        if (item.getOwnerId() != userId) {
            throw new AccessException("User ID = " + userId + " is not owner.");
        }
        Item foundedItem = itemStorage.getById(userId, itemId);
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty()) {
            itemDto.setDescription(foundedItem.getDescription());
        }
        if (itemDto.getName() == null || itemDto.getName().isEmpty()) {
            itemDto.setName(foundedItem.getName());
        }
        if (itemDto.getAvailable() == null) {
            itemDto.setAvailable(foundedItem.getAvailable());
        }
        Item result = itemStorage.update(owner.getId(), itemId, fromItemDto(itemDto, userId, null));
        return toItemDto(result);
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
        if (text.isEmpty()) {
            return new ArrayList<>();
            }
        return toItemDtoList(itemStorage.search(text.toLowerCase()));
    }
}
