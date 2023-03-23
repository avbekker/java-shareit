package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import java.util.List;

public interface ItemRequestService {

    ItemResponseDto create(long userId, ItemRequestDto itemRequestDto);

    ItemResponseDto getById(long userId, long requestId);

    List<ItemResponseDto> getAll(long userId, int from, int size);

    List<ItemResponseDto> getByUserId(long userId);
}
