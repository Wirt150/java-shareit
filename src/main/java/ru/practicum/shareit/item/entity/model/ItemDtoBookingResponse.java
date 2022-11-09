package ru.practicum.shareit.item.entity.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ItemDtoBookingResponse implements Serializable {
    private long id;
    private String name;
}
