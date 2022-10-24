package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private long id;
    private long userId;
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
}
