package ru.practicum.shareit.item.web;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.CommentDto;
import ru.practicum.shareit.item.entity.model.CommentDtoInfo;
import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;
import java.time.Instant;

@UtilityClass
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static CommentDto toCommentDto(CommentDtoInfo comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDto commentDto, long authorId, long itemId) {
        return Comment.builder()
                .item(Item.builder()
                        .id(itemId)
                        .build())
                .author(User.builder()
                        .id(authorId)
                        .build())
                .text(commentDto.getText())
                .created(Timestamp.from(Instant.now()))
                .build();
    }
}
