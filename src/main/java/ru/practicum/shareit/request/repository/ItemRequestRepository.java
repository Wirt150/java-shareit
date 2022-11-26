package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByAuthorIdOrderByCreatedAsc(Long authorId);

    Page<ItemRequest> findAllByAuthorIdNotOrderByCreatedAsc(Pageable pageable, Long authorId);
}
