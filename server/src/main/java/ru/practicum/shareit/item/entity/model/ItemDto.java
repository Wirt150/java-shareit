package ru.practicum.shareit.item.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {
    private long id;
    private long owner;
    private String name;
    private String description;
    private Boolean available;
    private ItemDtoBookingInfo lastBooking;
    private ItemDtoBookingInfo nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
