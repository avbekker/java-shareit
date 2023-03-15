package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@JsonTest
public class UserDtoTests {

    @Autowired
    JacksonTester<UserDto> json;

    @Test
    void userDtoTest() throws IOException {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("name@user.ru")
                .build();
        UserDto userDto = toUserDto(user);
        JsonContent<UserDto> result = json.write(userDto);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(user.getEmail());
    }
}
