package ru.practicum.shareit.booking.error;

public class ItemNotAvailableException extends RuntimeException {
    public ItemNotAvailableException(Long id) {
        super(String.format("Вещь с id: %s недоступна для бронирования.", id));
    }
}
