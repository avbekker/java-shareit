package ru.practicum.shareit.bookingTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTests {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = LocalDateTime.now().plusHours(2);
    BookingDtoResponse bookingDtoResponse;
    BookingDtoRequest bookingDtoRequest;

    @BeforeEach
    void beforeTest() {
        UserDto user = UserDto.builder()
                .id(1L)
                .name("user")
                .email("user@user.ru")
                .build();
        ItemDtoResponse item = ItemDtoResponse.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .build();
        bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(BookingDtoResponse.ItemForResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .build())
                .booker(BookingDtoResponse.UserForResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .build();
        bookingDtoRequest = BookingDtoRequest.builder()
                .id(1L)
                .start(start)
                .end(end)
                .itemId(1L)
                .build();
    }

    @SneakyThrows
    @Test
    void create() {
        when(bookingService.create(any(BookingDtoRequest.class), anyLong()))
                .thenReturn(bookingDtoResponse);
        String expected = objectMapper.writeValueAsString(bookingDtoResponse);
        String result = mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(result, expected);
    }

    @SneakyThrows
    @Test
    void createFailTime() {
        bookingDtoRequest.setEnd(start.minusHours(2));
        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    @Test
    void approve() {
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);
        when(bookingService.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoResponse);
        String expected = objectMapper.writeValueAsString(bookingDtoResponse);
        String result = mockMvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expected, result);
    }

    @SneakyThrows
    @Test
    void findById() {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(bookingDtoResponse);
        String expected = objectMapper.writeValueAsString(bookingDtoResponse);
        String result = mockMvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expected, result);
    }

    @SneakyThrows
    @Test
    void findByBooker() {
        when(bookingService.findByBooker(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));
        String expected = objectMapper.writeValueAsString(List.of(bookingDtoResponse));
        String result = mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expected, result);
    }

    @SneakyThrows
    @Test
    void findByBookerFailPagination() {
        when(bookingService.findByBooker(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));
        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("size", "1")
                        .param("from", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @SneakyThrows
    @Test
    void findByOwner() {
        when(bookingService.findByBooker(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));
        String expected = objectMapper.writeValueAsString(List.of(bookingDtoResponse));
        String result = mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(expected, result);
    }

    @SneakyThrows
    @Test
    void findByOwnerFailPagination() {
        when(bookingService.findByBooker(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));
        mockMvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("size", "0")
                        .param("from", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }
}
