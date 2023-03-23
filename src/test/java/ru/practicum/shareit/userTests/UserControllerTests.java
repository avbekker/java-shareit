package ru.practicum.shareit.userTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDtoList;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTests {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserDto userDto;
    User user;

    @BeforeEach
    void beforeTest() {
        userDto = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@user.ru")
                .build();
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@user.ru")
                .build();
    }

    @SneakyThrows
    @Test
    void create() {
        when(userService.create(any(UserDto.class)))
                .thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @SneakyThrows
    @Test
    void update() {
        when(userService.update(anyLong(), any(UserDto.class)))
                .thenReturn(userDto);
        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @SneakyThrows
    @Test
    void getById() {
        when(userService.getById(anyLong())).thenReturn(user);
        mockMvc.perform(get("/users/1").content(objectMapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(toUserDto(user))));
    }

    @SneakyThrows
    @Test
    void getAll() {
        when(userService.getAll()).thenReturn(List.of(user));
        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(toUserDtoList(List.of(user)))));
    }

    @SneakyThrows
    @Test
    void deleteById() {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void deleteAll() {
        mockMvc.perform(delete("/users"))
                .andExpect(status().isOk());
    }
}
