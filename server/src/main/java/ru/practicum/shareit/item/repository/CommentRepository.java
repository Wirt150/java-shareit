package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.model.CommentDtoInfo;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentDtoInfo> findAllByItemId(long itemId);
}
