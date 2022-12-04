package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
public class CommentDto implements Serializable {
    private long id;
    @NotEmpty(message = "Комментарий не может быть пустым")
    private String text;
    private String authorName;
    private Timestamp created;
}
