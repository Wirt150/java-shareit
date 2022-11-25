package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemService extends CommentService {
    Item add(Item item, long userId);

    Item getById(long id, long userId);

    List<Item> getAll(long userId, int from, int size);

    Item update(Item item, long id, long userId);

    List<Item> search(String text, int from, int size);
}
