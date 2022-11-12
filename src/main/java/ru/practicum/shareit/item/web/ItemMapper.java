package ru.practicum.shareit.item.web;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.ItemDto;
import ru.practicum.shareit.item.entity.model.ItemDtoBookingInfo;
import ru.practicum.shareit.user.entity.User;

import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking() != null ?
                        ItemDtoBookingInfo.builder()
                                .id(item.getLastBooking().getId())
                                .bookerId(item.getLastBooking().getBooker().getId())
                                .build() : null)
                .nextBooking(item.getNextBooking() != null ?
                        ItemDtoBookingInfo.builder()
                                .id(item.getNextBooking().getId())
                                .bookerId(item.getNextBooking().getBooker().getId())
                                .build() : null)
                .comments(item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()))
                .build();
    }


    public static Item toItem(ItemDto itemDto, long userId) {
        return Item.builder()
                .id(itemDto.getId())
                .owner(User.builder()
                        .id(userId)
                        .build())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

}
