package ru.practicum.shareit.booking.entity.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoResponse implements Serializable {
    private final long itemId;
    private final LocalDateTime start;
    private final LocalDateTime end;
}
