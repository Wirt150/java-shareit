package ru.practicum.shareit.item.error;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super(String.format("Вещь с id:%s не найдена.", id));
    }
}
