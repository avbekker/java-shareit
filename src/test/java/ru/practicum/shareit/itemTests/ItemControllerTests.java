package ru.practicum.shareit.itemTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTests {

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDtoResponse itemDtoResponse;
    private CommentDto commentDto;

    @BeforeEach
    void prepareBeforeEach() {
        itemDtoResponse = ItemDtoResponse.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .build();
        commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .build();
    }

    @SneakyThrows
    @Test
    void create() {
        when(itemService.create(anyLong(), any(ItemDtoRequest.class)))
                .thenReturn(itemDtoResponse);
        String result = mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDtoResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDtoResponse), result);
    }

    @SneakyThrows
    @Test
    void update() {
        when(itemService.update(anyLong(), anyLong(), any(ItemDtoRequest.class)))
                .thenReturn(itemDtoResponse);
        String result = mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(itemDtoResponse))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDtoResponse), result);
    }

    @SneakyThrows
    @Test
    void getAll() {
        when(itemService.getAll(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoResponse));
        String result = mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(List.of(itemDtoResponse)), result);
    }

    @SneakyThrows
    @Test
    void getById() {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDtoResponse);
        String result = mockMvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(itemDtoResponse), result);
    }

    @SneakyThrows
    @Test
    void deleteById() {
        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void search() {
        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoResponse));
        String result = mockMvc.perform(get("/items/search?text='name'")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(List.of(itemDtoResponse)), result);
    }

    @SneakyThrows
    @Test
    void createComment() {
        when(commentService.create(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(commentDto);
        String result = mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}
