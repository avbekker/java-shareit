package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.toItemRequest;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.toItemRequestDto;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    private final ItemRequestRepository requestRepository;

    @Transactional
    @Override
    public ItemResponseDto create(long userId, ItemRequestDto itemRequestDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID = " + userId + " not found."));
        ItemRequest itemRequest = toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setCreatorId(userId);
        return toItemRequestDto(requestRepository.save(itemRequest));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemResponseDto getById(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID = " + userId + " not found."));
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request ID = " + requestId + " not found."));
        ItemResponseDto itemResponseDto = toItemRequestDto(itemRequest);
        List<ItemDtoResponse> items = itemRepository.findAllByRequestId(itemResponseDto.getId())
                .stream().map(ItemMapper::toItemDtoResponse).collect(toList());
        itemResponseDto.setItems(items);
        return itemResponseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemResponseDto> getAll(long userId, int size, int from) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID = " + userId + " not found."));
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<ItemRequest> result = requestRepository.findAllByCreatorIdNotLike(userId, pageRequest);
        return addItemsToRequestList(result);

    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemResponseDto> getByUserId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User ID = " + userId + " not found."));
        List<ItemRequest> result = requestRepository.findAllByCreatorIdOrderByCreatedAsc(userId);
        return addItemsToRequestList(result);
    }

    private List<ItemResponseDto> addItemsToRequestList(List<ItemRequest> itemRequests) {
        Map<ItemRequest, List<Item>> allItems = itemRepository.findAllByRequestIn(itemRequests)
                .stream().collect(groupingBy(Item::getRequest, toList()));
        return itemRequests.stream().map(request -> {
            ItemResponseDto requestDto = toItemRequestDto(request);
            List<ItemDtoResponse> items = allItems.getOrDefault(request, List.of())
                    .stream().map(ItemMapper::toItemDtoResponse).collect(toList());
            requestDto.setItems(items);
            return requestDto;
        }).collect(toList());
    }
}
