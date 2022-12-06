package ru.practicum.shareit.booking.entity.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.item.entity.model.ItemDtoBookingResponse;
import ru.practicum.shareit.user.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoRequest implements Serializable {
    private final long id;
    private final User booker;
    private final ItemDtoBookingResponse item;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final BookingStatus status;
}