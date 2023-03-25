package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
                                  @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("Received POST request for new Item of User ID = {}", userId);
        return itemService.create(userId, itemDtoRequest);
    }

    @PatchMapping("/{id}")
    public ItemDtoResponse update(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long id, @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("Received PATCH request for Item ID = {} of User ID = {}", id, userId);
        return itemService.update(userId, id, itemDtoRequest);
    }

    @GetMapping
    public List<ItemDtoResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from) {
        log.info("Received GET request for all Items of User ID = {}", userId);
        return itemService.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ItemDtoResponse getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Received GET request for Item ID = {} of User ID = {}", id, userId);
        return itemService.getById(userId, id);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> search(@RequestParam String text,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from) {
        log.info("Received GET request for searching items by text = {}", text);
        if (text.isBlank()) {
            return List.of();
        }
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("Received POST request for comment on Item ID = {} from User ID = {}", itemId, userId);
        return commentService.create(userId, itemId, commentDto);
    }
}
