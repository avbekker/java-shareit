package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        log.info("Received POST request for new Item of User ID = {}", userId);
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id, @RequestBody ItemDto itemDto) {
        log.info("Received PATCH request for Item ID = {} of User ID = {}", id, userId);
        return itemService.update(userId, id, itemDto);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Received GET request for all Items of User ID = {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Received GET request for Item ID = {} of User ID = {}", id, userId);
        return itemService.getById(userId, id);
    }

    @DeleteMapping
    public void deleteAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Received DELETE request for all Items of User ID = {}", userId);
        itemService.deleteAll(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Received DELETE request for Item ID = {} of User ID = {}", id, userId);
        itemService.deleteById(userId, id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Received GET request for searching items by text = {}", text);
        return itemService.search(text);
    }
}
