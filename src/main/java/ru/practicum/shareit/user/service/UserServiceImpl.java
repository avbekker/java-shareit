package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.mapper.UserMapper.fromUserDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        if (userStorage.getAll().contains(fromUserDto(userDto))) {
            throw new AlreadyExistException("User already exist.");
        }
        if (isEmailAlreadyExist(userDto.getEmail(), 0)) {
            throw new EmailConflictException("Email " + userDto.getEmail() + " already exist.");
        }
        User user = userStorage.create(fromUserDto(userDto));
        return toUserDto(user);
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        Optional<User> foundedUser = userStorage.getById(id);
        if (foundedUser.isEmpty()) {
            throw new NotFoundException("User ID = " + id + " not found.");
        }
        if (isEmailAlreadyExist(userDto.getEmail(), id)) {
            throw new EmailConflictException("Email " + userDto.getEmail() + " already exist.");
        }
        String userEmail = foundedUser.get().getEmail();
        String userName = foundedUser.get().getName();
        User user = fromUserDto(userDto);
        if (userDto.getEmail() == null) {
            user.setEmail(userEmail);
        }
        if (userDto.getName() == null) {
            user.setName(userName);
        }
        return toUserDto(userStorage.update(id, user));
    }

    @Override
    public User getById(long id) {
        return userStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("User ID = " + id + " not found."));
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void deleteById(long id) {
        if (userStorage.getById(id).isEmpty()) {
            throw new NotFoundException("User ID = " + id + " not found.");
        }
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
