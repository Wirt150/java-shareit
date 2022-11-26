package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description, Pageable pageable);

    List<Item> findAllByOwnerIdOrderByIdAsc(long id, Pageable pageable);

    List<Item> findAllByRequestId(long id);

}
