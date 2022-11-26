package ru.practicum.shareit.request.web;

import ru.practicum.shareit.item.entity.model.ItemDtoRequestInfo;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.entity.model.ItemRequestDto;
import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated().toLocalDateTime())
                .items(itemRequest.getItems().stream()
                        .map(req -> ItemDtoRequestInfo.builder()
                                .id(req.getId())
                                .name(req.getName())
                                .description(req.getDescription())
                                .available(req.getAvailable())
                                .requestId(req.getRequest().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, long authorId) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .author(User.builder()
                        .id(authorId)
                        .build())
                .description(itemRequestDto.getDescription())
                .created(Timestamp.from(Instant.now()))
                .items(new ArrayList<>())
                .build();
    }
}
