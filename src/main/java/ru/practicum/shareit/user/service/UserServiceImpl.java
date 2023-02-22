package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static ru.practicum.shareit.user.mapper.UserMapper.fromUserDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        if (isEmailAlreadyExist(userDto.getEmail(), 0)) {
            throw new EmailConflictException("Email " + userDto.getEmail() + " already exist.");
        }
        User user = userStorage.create(fromUserDto(userDto));
        return toUserDto(user);
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        User foundedUser = userStorage.getById(id);
        if (isEmailAlreadyExist(userDto.getEmail(), id)) {
            throw new EmailConflictException("Email " + userDto.getEmail() + " already exist.");
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            foundedUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            foundedUser.setName(userDto.getName());
        }
        return toUserDto(foundedUser);
    }

    @Override
    public User getById(long id) {
        return userStorage.getById(id);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void deleteById(long id) {
        userStorage.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userStorage.deleteAll();
    }

    private boolean isEmailAlreadyExist(String email, long userId) {
        return userStorage.getAll().stream()
                .anyMatch(user -> user.getEmail().equals(email) && user.getId() != userId);
    }
}
