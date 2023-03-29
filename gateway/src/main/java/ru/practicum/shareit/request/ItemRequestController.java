package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validator.OnCreate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Validated(OnCreate.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("ItemRequestClient: Received POST request for new ItemRequest of User ID = {}", userId);
        return client.create(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long requestId) {
        log.info("ItemRequestClient: Received GET request for ItemRequest ID = {} by User ID = {}", requestId, userId);
        return client.getById(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("ItemRequestClient: Received GET request for all ItemRequests by User ID = {}, size = {}, from = {}", userId, size, from);
        return client.getAll(userId, size, from);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemRequestClient: Received GET request for ItemRequests of User ID = {}", userId);
        return client.getByUserId(userId);
    }
}
