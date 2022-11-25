package ru.practicum.shareit.item.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.CommentDto;
import ru.practicum.shareit.item.entity.model.CommentDtoInfo;
import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2022, 10, 10, 10, 10));
    private final User user = User.builder()
            .id(1L)
            .email("test@test.test")
            .name("Name")
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("TestName")
            .description("testDescription")
            .owner(user)
            .available(true)
            .build();
    private Comment commentTest;

    @BeforeEach
    void setUp() {
        commentTest = Comment.builder()
                .id(1L)
                .item(item)
                .author(user)
                .text("test")
                .created(timestamp)
                .build();
    }

    @Test
    @DisplayName("Проверяем методы маппера Comment.")
    void whenCheckItemMapper() {
        final CommentDto commentDto = CommentMapper.toCommentDto(commentTest);

        //test
        assertThat(commentDto, notNullValue());
        assertThat(commentDto.getId(), equalTo(commentTest.getId()));
        assertThat(commentDto.getText(), equalTo(commentTest.getText()));
        assertThat(commentDto.getAuthorName(), equalTo(commentTest.getAuthor().getName()));
        assertThat(commentDto.getCreated(), equalTo(commentTest.getCreated()));

        final Comment comment = CommentMapper.toComment(commentDto, user.getId(), item.getId());

        //test
        assertThat(comment, notNullValue());
        assertThat(comment.getText(), equalTo(commentTest.getText()));
        assertThat(comment.getAuthor(), equalTo(commentTest.getAuthor()));
        assertNotNull(comment.getCreated());

        final CommentDtoInfo commentDtoInfo = new CommentDtoInfo() {
            @Override
            public long getId() {
                return 1L;
            }

            @Override
            public String getText() {
                return "test";
            }

            @Override
            public User getAuthor() {
                return user;
            }

            @Override
            public Timestamp getCreated() {
                return timestamp;
            }
        };
        final CommentDto commentDtoByInfo = CommentMapper.toCommentDto(commentDtoInfo);

        //test
        assertThat(commentDtoByInfo, notNullValue());
        assertThat(commentDtoByInfo.getId(), equalTo(commentTest.getId()));
        assertThat(commentDtoByInfo.getText(), equalTo(commentTest.getText()));
        assertThat(commentDtoByInfo.getAuthorName(), equalTo(commentTest.getAuthor().getName()));
        assertThat(commentDtoByInfo.getCreated(), equalTo(commentTest.getCreated()));
    }
}
