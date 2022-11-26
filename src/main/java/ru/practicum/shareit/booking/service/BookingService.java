package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.entity.Booking;

import java.util.List;

public interface BookingService {

    Booking add(Booking booking, long bookerId);

    Booking getById(long id, long userId);

    List<Booking> findAll(long bookerId, String state, int from, int size);

    List<Booking> findAllBookingByOwner(long ownerId, String state, int from, int size);

    Booking approved(long id, long owner, boolean approved);
}
