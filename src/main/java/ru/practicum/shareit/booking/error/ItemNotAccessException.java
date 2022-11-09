package ru.practicum.shareit.booking.error;

import javax.persistence.EntityNotFoundException;

public class ItemNotAccessException extends EntityNotFoundException {
    public ItemNotAccessException(Long id) {
        super(String.format("Бронирование с id: %s не доступно для этого пользователя.", id));
    }
}
