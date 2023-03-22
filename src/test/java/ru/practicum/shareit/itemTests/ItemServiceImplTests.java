package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemServiceImplTests {

    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRequestRepository requestRepository = mock(ItemRequestRepository.class);
    private final ItemService service = new ItemServiceImpl(itemRepository, commentRepository, bookingRepository,
            userRepository, requestRepository);

    @Test
    void create() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder().name("item").description("item desc").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        ItemDtoResponse result = service.create(owner.getId(), itemDtoRequest);
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getOwner().getId(), result.getOwner().getId());
    }

    @Test
    void update() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder().name("item update").description("item desc update").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ItemDtoResponse result = service.update(owner.getId(), item.getId(), itemDtoRequest);
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(itemDtoRequest.getName(), result.getName());
        assertEquals(itemDtoRequest.getDescription(), result.getDescription());
    }

    @Test
    void updateAccessFail() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder().name("item update").description("item desc update").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        assertThrows(AccessException.class, () -> service.update(2, item.getId(), itemDtoRequest));
    }

    @Test
    void updateNoDesc() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder().name("item update").description(null).available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ItemDtoResponse result = service.update(owner.getId(), item.getId(), itemDtoRequest);
        assertNotNull(result);
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    void updateNoName() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder().name(null).description("item desc update").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ItemDtoResponse result = service.update(owner.getId(), item.getId(), itemDtoRequest);
        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
    }

    @Test
    void updateNoAvailable() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder().name("item update").description("item desc update").available(true).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ItemDtoResponse result = service.update(owner.getId(), item.getId(), itemDtoRequest);
        assertNotNull(result);
        assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void getById() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ItemDtoResponse result = service.getById(owner.getId(), item.getId());
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
    }

    @Test
    void getAll() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.findAllByOwnerId(anyLong(), any())).thenReturn(List.of(item));
        List<ItemDtoResponse> result = service.getAll(owner.getId(), 0, 1);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void search() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        Item item = Item.builder().id(1L).name("item").description("item desc").available(true).owner(owner).build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        when(itemRepository.search(anyString(), any())).thenReturn(List.of(item));
        List<ItemDtoResponse> result = service.search("item", 0, 1);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void searchFail() {
        User owner = User.builder().id(1L).name("user").email("user@user.ru").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(owner));
        List<ItemDtoResponse> result = service.search("", 0, 1);
        assertTrue(result.isEmpty());
    }
}
