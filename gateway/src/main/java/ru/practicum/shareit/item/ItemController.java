package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validator.OnCreate;
import ru.practicum.shareit.validator.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@Validated
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Validated(OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("ItemClient: Received POST request for new Item of User ID = {}", userId);
        return client.createItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long id, @Validated(OnUpdate.class) @RequestBody ItemDto itemDto) {
        log.info("ItemClient: Received PATCH request for Item ID = {} of User ID = {}", id, userId);
        return client.updateItem(itemDto, id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from) {
        log.info("ItemClient: Received GET request for all Items of User ID = {}", userId);
        return client.getAllItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("ItemClient: Received GET request for Item ID = {} of User ID = {}", id, userId);
        return client.getItemById(id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from) {
        log.info("ItemClient: Received GET request for searching items by text = {}", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return client.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("ItemClient: Received POST request for comment on Item ID = {} from User ID = {}", itemId, userId);
        return client.createComment(itemId, userId, commentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("ItemClient: Received GET request for Item ID = {} of User ID = {}", id, userId);
        return client.deleteItem(id, userId);
    }
}
