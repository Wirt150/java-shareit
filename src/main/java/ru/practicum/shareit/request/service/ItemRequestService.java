package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest add(ItemRequest toItemRequest, long authorId);

    ItemRequest getById(long id, long authorId);

    List<ItemRequest> getAll(long userId);

    List<ItemRequest> getPage(int from, int size, long userId);
}
