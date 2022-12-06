package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ItemDtoBookingInfo implements Serializable {
    private long id;
    private long bookerId;
}
