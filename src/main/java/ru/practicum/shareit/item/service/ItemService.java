package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemService {
    Item add(Item item, long userId);

    Item getById(long id, long userId);

    List<Item> getAll(long userId);

    Item update(Item item, long id, long userId);

    List<Item> search(String text);

    Comment addComment(Comment dto, long author, long itemId);
}
