package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items;
    private Long id = 1L;

    @Override
    public Optional<Item> addDto(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        return Optional.of(items.get(item.getId()));
    }

    @Override
    public Optional<Item> getDtoById(long id) {
        if (items.containsKey(id)) {
            return Optional.of(items.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAllDto(long userId) {
        return items.values().stream()
                .filter(item -> item.getUserId() == userId)
                .sorted(Comparator.comparingLong(Item::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> updateDto(Item item, long id) {
        if (item.getName() != null) {
            items.get(id).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(id).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            items.get(id).setAvailable(item.getAvailable());
        }
        return Optional.of(items.get(id));
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(i -> (i.getName().toLowerCase().contains(text)
                        || i.getDescription().toLowerCase().contains(text)) && i.getAvailable())
                .collect(Collectors.toList());
    }
}
