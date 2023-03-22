package ru.practicum.shareit.requestTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.toItemRequestDto;

@JsonTest
public class ItemRequestDtoTests {

    @Autowired
    JacksonTester<ItemRequestDto> json;

    @Test
    void itemRequestDtoTest() throws IOException {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1)
                .description("description")
                .created(LocalDateTime.of(2023, 3, 15, 23, 0, 0))
                .creatorId(1L)
                .build();
        ItemRequestDto itemRequestDto = toItemRequestDto(itemRequest);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequest.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
