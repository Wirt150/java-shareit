package ru.practicum.shareit.item.error;

import javax.persistence.EntityNotFoundException;

public class ItemNotFoundByUserException extends EntityNotFoundException {
    public ItemNotFoundByUserException(final long id, final long userId) {
        super(String.format("Вещь с id: %s у пользователя с id: %s не найдена.", id, userId));
    }
}
