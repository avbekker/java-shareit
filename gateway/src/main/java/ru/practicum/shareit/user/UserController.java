package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.validator.OnCreate;
import ru.practicum.shareit.validator.OnUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {

    private final  UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(OnCreate.class) @RequestBody UserDtoRequest userDto) {
        log.info("UserClient: Received POST request for new User.");
        return client.create(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id, @Validated(OnUpdate.class) @RequestBody UserDtoRequest userDto) {
        log.info("UserClient: Received PUT request for User ID = {}", id);
        return client.update(id, userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        log.info("UserClient: Received GET request for User ID = {}", id);
        return client.getById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("UserClient: Received GET request for all Users");
        return client.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable long id) {
        log.info("UserClient: Received DELETE request for User ID = {}", id);
        return client.delete(id);
    }
}
