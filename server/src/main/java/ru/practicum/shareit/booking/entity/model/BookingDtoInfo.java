package ru.practicum.shareit.booking.entity.model;

import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.user.entity.User;

public interface BookingDtoInfo {
    long getId();

    User getBooker();

    BookingStatus getStatus();
}
