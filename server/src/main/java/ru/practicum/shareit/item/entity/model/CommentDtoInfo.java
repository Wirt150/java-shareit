package ru.practicum.shareit.item.entity.model;

import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;

public interface CommentDtoInfo {
    long getId();

    String getText();

    User getAuthor();

    Timestamp getCreated();
}

