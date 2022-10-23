package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;


    @Override
    public Optional<Item> addDto(Item item, long userId) {
        userService.getDtoById(userId);
        item.setUserId(userId);
        return itemRepository.addDto(item);
    }

    @Override
    public Optional<Item> getDtoById(long id) {
        return Optional.of(itemRepository.getDtoById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }

    @Override
    public List<Item> getAllDto(long userId) {
        return itemRepository.getAllDto(userId);
    }

    @Override
    public Optional<Item> updateDto(Item item, long id, long userId) {
        itemRepository.getDtoById(id).map(Item::getUserId).filter(s -> s == userId)
                .orElseThrow(() -> new ItemNotFoundByUserException(id, userId));
        return itemRepository.updateDto(item, id);
    }

    @Override
    public List<Item> search(String text) {
        if (text.isEmpty()) return Collections.emptyList();
        return itemRepository.search(text.toLowerCase());
    }
}

