package ru.practicum.shareit.item.error;

public class CommentIllegalException extends RuntimeException {
    public CommentIllegalException(long authorId, long itemId) {
        super(String.format("Пользователь с id: %s не может оставить комментарий для вещи id: %s.", authorId, itemId));
    }
}
