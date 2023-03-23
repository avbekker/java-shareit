package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoTests {
    @Autowired
    JacksonTester<CommentDto> json;

    @Test
    void commentDtoTest() throws IOException {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("name")
                .created(LocalDateTime.of(2023, 3, 22, 23, 0, 0))
                .build();
        JsonContent<CommentDto> result = json.write(commentDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("text");
    }
}
