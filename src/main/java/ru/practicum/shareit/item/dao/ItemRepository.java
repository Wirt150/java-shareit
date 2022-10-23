package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> addDto(Item item);

    Optional<Item> getDtoById(long id);

    List<Item> getAllDto(long userId);

    Optional<Item> updateDto(Item item, long id);

    List<Item> search(String text);
}
