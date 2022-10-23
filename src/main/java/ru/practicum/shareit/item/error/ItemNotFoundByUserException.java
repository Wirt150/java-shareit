package ru.practicum.shareit.item.error;

public class ItemNotFoundByUserException extends RuntimeException{
    public ItemNotFoundByUserException(final long id, final long userId){
        super(String.format("Вещь с id:%s у пользователя с id:%s не найдена.", id, userId));
    }
}
