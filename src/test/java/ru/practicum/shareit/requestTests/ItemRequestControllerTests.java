package ru.practicum.shareit.requestTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTests {

    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void create() {
        ItemResponseDto requestDto = ItemResponseDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .description("description")
                .build();
        when(requestService.create(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(requestDto);
        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }

    @SneakyThrows
    @Test
    void getById() {
        ItemResponseDto requestDto = ItemResponseDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
        when(requestService.getById(anyLong(), anyLong()))
                .thenReturn(requestDto);
        String result = mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }

    @SneakyThrows
    @Test
    void getAll() {
        ItemResponseDto requestDto = ItemResponseDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
        when(requestService.getAll(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestDto));
        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @SneakyThrows
    @Test
    void getByUserId() {
        ItemResponseDto requestDto = ItemResponseDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
        when(requestService.getByUserId(anyLong()))
                .thenReturn(List.of(requestDto));
        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }
}
