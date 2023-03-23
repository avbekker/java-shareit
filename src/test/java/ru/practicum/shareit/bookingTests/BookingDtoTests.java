package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTests {

    @Autowired
    private JacksonTester<BookingDtoResponse> json;

    @Autowired
    private JacksonTester<ShortBookingDto> shortJson;

    @Test
    void bookingDtoResponseTest() throws IOException {
        BookingDtoResponse bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 3, 15, 22, 0, 0))
                .end(LocalDateTime.of(2023, 3, 15, 23, 0, 0))
                .item(null)
                .booker(null)
                .status(null)
                .build();
        JsonContent<BookingDtoResponse> result = json.write(bookingDtoResponse);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2023, 3, 15, 22, 0, 0)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2023, 3, 15, 23, 0, 0)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    void shortBookingDtoTest() throws IOException {
        ShortBookingDto shortBookingDto = ShortBookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 3, 15, 22, 0, 0))
                .end(LocalDateTime.of(2023, 3, 15, 23, 0, 0))
                .status(null)
                .build();
        JsonContent<ShortBookingDto> result = shortJson.write(shortBookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.of(2023, 3, 15, 22, 0, 0)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.of(2023, 3, 15, 23, 0, 0)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }
}
