package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> addDto(User user) {
        return Optional.of(userRepository.addDto(user)
                .orElseThrow(() -> new UserRepeatEmailException(user.getEmail())));
    }

    @Override
    public Optional<User> getDtoById(long id) {
        return Optional.of(userRepository.getDtoById(id)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public List<User> getAllDto() {
        return userRepository.getAllDto();
    }

    @Override
    public Optional<User> updateDto(User user, long id) {
        return Optional.of(userRepository.updateDto(user, id)
                .orElseThrow(() -> new UserRepeatEmailException(user.getEmail())));
    }

    @Override
    public void deleteDto(long id) {
        userRepository.deleteDto(id);
    }
}
