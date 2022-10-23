package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Optional<Item> addDto(Item item, long userId);

    Optional<Item> getDtoById(long id);

    List<Item> getAllDto(long userId);

    Optional<Item> updateDto(Item item, long id, long userId);

    List<Item> search(String text);
}
