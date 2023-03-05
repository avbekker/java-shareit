package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.OnCreate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDtoResponse create(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @Validated(OnCreate.class) @RequestBody ItemDtoResponse itemDtoResponse) {
        log.info("Received POST request for new Item of User ID = {}", userId);
        return itemService.create(userId, itemDtoResponse);
    }

    @PatchMapping("/{id}")
    public ItemDtoResponse update(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long id, @RequestBody ItemDtoResponse itemDtoResponse) {
        log.info("Received PATCH request for Item ID = {} of User ID = {}", id, userId);
        return itemService.update(userId, id, itemDtoResponse);
    }

    @GetMapping
    public List<ItemDtoResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Received GET request for all Items of User ID = {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDtoResponse getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Received GET request for Item ID = {} of User ID = {}", id, userId);
        return itemService.getById(userId, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Received DELETE request for Item ID = {} of User ID = {}", id, userId);
        itemService.deleteById(userId, id);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestParam String text) {
        log.info("Received GET request for searching items by text = {}", text);
        if (text.isBlank()) {
            return List.of();
        }
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long itemId,
                                    @Validated(OnCreate.class) @RequestBody CommentDto commentDto) {
        log.info("Received POST request for comment on Item ID = {} from User ID = {}", itemId, userId);
        return commentService.create(userId, itemId, commentDto);
    }
}
