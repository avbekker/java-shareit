package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemResponseDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @RequestBody ItemRequestDto itemRequestDto) {
        log.info("SERVER: Received POST request for new ItemRequest of User ID = {}", userId);
        return requestService.create(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemResponseDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long requestId) {
        log.info("SERVER: Received GET request for ItemRequest ID = {} by User ID = {}", requestId, userId);
        return requestService.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(value = "from", defaultValue = "0") int from,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("SERVER: Received GET request for all ItemRequests by User ID = {}, size = {}, from = {}", userId, size, from);
        return requestService.getAll(userId, size, from);
    }

    @GetMapping
    public List<ItemResponseDto> getByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("SERVER: Received GET request for ItemRequests of User ID = {}", userId);
        return requestService.getByUserId(userId);
    }
}
