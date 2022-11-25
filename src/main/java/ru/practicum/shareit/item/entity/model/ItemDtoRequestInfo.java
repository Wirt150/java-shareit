package ru.practicum.shareit.item.entity.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ItemDtoRequestInfo implements Serializable {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;
}
