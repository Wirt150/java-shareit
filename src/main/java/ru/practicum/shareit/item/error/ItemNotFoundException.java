package ru.practicum.shareit.item.error;

import javax.persistence.EntityNotFoundException;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(Long id) {
        super(String.format("Вещь с id: %s не найдена.", id));
    }
}
