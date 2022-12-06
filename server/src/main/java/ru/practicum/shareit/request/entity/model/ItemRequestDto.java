package ru.practicum.shareit.request.entity.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.entity.model.ItemDtoRequestInfo;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDtoRequestInfo> items;
}
