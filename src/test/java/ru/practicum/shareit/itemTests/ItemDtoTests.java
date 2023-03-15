package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoTests {

    @Autowired
    JacksonTester<ItemDtoResponse> json;

    @Test
    void itemDtoResponseTest() throws IOException {
        ItemDtoResponse itemDtoResponse = ItemDtoResponse.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .comments(null)
                .owner(null)
                .lastBooking(null)
                .nextBooking(null)
                .requestId(null)
                .build();
        JsonContent<ItemDtoResponse> result = json.write(itemDtoResponse);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isTrue();
    }
}
