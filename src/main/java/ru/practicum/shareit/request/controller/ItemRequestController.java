package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.OnCreate;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Validated(OnCreate.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Received POST request for new ItemRequest of User ID = {}", userId);
        return requestService.create(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long requestId) {
        log.info("Received GET request for ItemRequest ID = {} by User ID = {}", requestId, userId);
        return requestService.getById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                       @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from) {
        log.info("Received GET request for all ItemRequests by User ID = {}", userId);
        return requestService.getAll(userId, size, from);
    }

    @GetMapping
    public List<ItemRequestDto> getByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Received GET request for ItemRequests of User ID = {}", userId);
        return requestService.getByUserId(userId);
    }
}
