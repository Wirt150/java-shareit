package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.entity.Comment;

public interface CommentService {
    Comment addComment(Comment dto, long author, long itemId);
}
