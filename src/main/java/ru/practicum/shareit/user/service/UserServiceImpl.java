package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User add(final User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new UserRepeatEmailException(user.getEmail());
        }
    }

    @Override
    public User getById(final long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(final User user, final long id) {
        User userUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        Optional.ofNullable(user.getName()).ifPresent(userUpdate::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(userUpdate::setEmail);
        return userRepository.save(userUpdate);
    }

    @Override
    public void delete(final long id) {
        userRepository.deleteById(id);
    }
}
