package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    private User user;
    private ItemRequest request;
    private ItemRequest secondRequest;

    @BeforeEach
    void prepareBeforeEach() {
        user = userRepository.save(User.builder().name("user").email("user@user.ru").build());
        User secondUser = userRepository.save(User.builder().name("secondUser").email("secondUser@secondUser.ru").build());
        request = requestRepository.save(ItemRequest.builder().description("description").creatorId(user.getId()).created(LocalDateTime.now()).build());
        secondRequest = requestRepository.save(ItemRequest.builder().description("second item description").creatorId(secondUser.getId()).created(LocalDateTime.now()).build());
        itemRepository.save(Item.builder().name("item").description("description").available(true).owner(user).request(request).build());
        itemRepository.save(Item.builder().name("secondItem").description("second item description").available(true).owner(secondUser).request(secondRequest).build());
    }

    @AfterEach
    void cleanAfterEach() {
        requestRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void search() {
        List<Item> result = itemRepository.search("nothing", PageRequest.ofSize(1));
        assertTrue(result.isEmpty());
        result = itemRepository.search("second", PageRequest.ofSize(1));
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAllByOwnerId() {
        List<Item> result = itemRepository.findAllByOwnerId(user.getId(), PageRequest.ofSize(1));
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAllByRequestId() {
        List<Item> result = itemRepository.findAllByRequestId(secondRequest.getId());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        result = itemRepository.findAllByRequestId(request.getId());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAllByRequestIn() {
        List<Item> result = itemRepository.findAllByRequestIn(List.of(request, secondRequest));
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }
}
