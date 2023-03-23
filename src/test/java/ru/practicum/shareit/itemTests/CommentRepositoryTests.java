package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CommentRepositoryTests {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Item item;
    private Item secondItem;

    @BeforeEach
    void prepareBeforeEach() {
        User user = userRepository.save(User.builder().name("user").email("user@user.ru").build());
        User secondUser = userRepository.save(User.builder().name("secondUser").email("secondUser@secondUser.ru").build());
        item = itemRepository.save(Item.builder().name("item").description("description").available(true).owner(user).build());
        secondItem = itemRepository.save(Item.builder().name("secondItem").description("secondItem description").available(true).owner(secondUser).build());
        commentRepository.save(Comment.builder().author(user).created(LocalDateTime.now()).text("text").item(item).build());
    }

    @AfterEach
    void cleanAfterEach() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void findAllByItemId() {
        List<Comment> result = commentRepository.findAllByItemId(item.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
        List<Comment> result1 = commentRepository.findAllByItemId(secondItem.getId());
        assertNotNull(result1);
        assertEquals(0, result1.size());
    }

    @Test
    void findAllByItemIn() {
        List<Comment> result = commentRepository.findAllByItemIn(List.of(item, secondItem), Sort.by(Sort.Direction.DESC, "created"));
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
