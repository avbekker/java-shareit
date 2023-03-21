package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class RequestRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository requestRepository;

    private User user;

    @BeforeEach
    void prepareBeforeEach() {
        user = userRepository.save(User.builder().name("user").email("user@user.ru").build());
        itemRepository.save(Item.builder().name("item").description("description").available(true).owner(user).build());
        requestRepository.save(ItemRequest.builder().description("description").creatorId(user.getId()).created(LocalDateTime.now()).build());
    }

    @AfterEach
    void cleanAfterEach() {
        itemRepository.deleteAll();
        requestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByCreatorIdOrderByCreatedAsc() {
        List<ItemRequest> result = requestRepository.findAllByCreatorIdOrderByCreatedAsc(user.getId());
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAllByCreatorIdNotLike() {
        User secondUser = userRepository.save(User.builder().name("secondUser").email("secondUser@secondUser.ru").build());
        requestRepository.save(ItemRequest.builder().description("description").creatorId(secondUser.getId()).created(LocalDateTime.now()).build());
        List<ItemRequest> result = requestRepository.findAllByCreatorIdNotLike(user.getId(), Pageable.ofSize(1));
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
