package ru.practicum.shareit.user.error;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Long id) {
        super(String.format("Пользователь с id: %s не найден.", id));
    }
}
