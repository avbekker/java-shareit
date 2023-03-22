package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTests {

    private final UserRepository repository = mock(UserRepository.class);
    private final UserService service = new UserServiceImpl(repository);

    @Test
    void create() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        UserDto userDto = UserDto.builder().name("user").email("user@user.ru").build();
        when(repository.save(any(User.class))).thenReturn(user);
        UserDto result = service.create(userDto);
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void update() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        User userUpdate = User.builder().id(1L).name("update user").email("update.user@user.ru").build();
        UserDto userDto = UserDto.builder().name("update user").email("update.user@user.ru").build();
        when(repository.save(any(User.class))).thenReturn(userUpdate);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        UserDto result = service.update(user.getId(), userDto);
        assertNotNull(result);
        assertEquals(userUpdate.getId(), result.getId());
        assertEquals(userUpdate.getName(), result.getName());
        assertEquals(userUpdate.getEmail(), result.getEmail());
    }

    @Test
    void getById() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        User result = service.getById(user.getId());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getAll() {
        User user = User.builder().id(1L).name("user").email("user@user.ru").build();
        when(repository.findAll()).thenReturn(List.of(user));
        List<User> result = service.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
