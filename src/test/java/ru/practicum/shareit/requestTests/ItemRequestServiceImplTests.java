package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.toItemRequest;

public class ItemRequestServiceImplTests {
    private final UserRepository userRepository = mock(UserRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final ItemRequestRepository requestRepository = mock(ItemRequestRepository.class);

    private final ItemRequestService service = new ItemRequestServiceImpl(userRepository, itemRepository, requestRepository);

    @Test
    void create() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().description("desc").build();
        ItemRequest itemRequest = toItemRequest(itemRequestDto);
        when(requestRepository.save(any())).thenReturn(itemRequest);
        ItemRequestDto result = service.create(user.getId(), itemRequestDto);
        assertNotNull(result);
        assertEquals(itemRequest.getDescription(), result.getDescription());
    }

    @Test
    void getById() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("desc").build();
        ItemRequest itemRequest = toItemRequest(itemRequestDto);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        ItemRequestDto result = service.getById(user.getId(), itemRequest.getId());
        assertNotNull(result);
        assertEquals(itemRequest.getDescription(), result.getDescription());
    }

    @Test
    void getAll() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("desc").build();
        ItemRequest itemRequest = toItemRequest(itemRequestDto);
        when(requestRepository.findAllByCreatorIdNotLike(anyLong(), any())).thenReturn(List.of(itemRequest));
        List<ItemRequestDto> result = service.getAll(user.getId(), 1, 1);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getByUserId() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("desc").build();
        ItemRequest itemRequest = toItemRequest(itemRequestDto);
        when(requestRepository.findAllByCreatorIdOrderByCreatedAsc(anyLong())).thenReturn(List.of(itemRequest));
        List<ItemRequestDto> result = service.getByUserId(user.getId());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
