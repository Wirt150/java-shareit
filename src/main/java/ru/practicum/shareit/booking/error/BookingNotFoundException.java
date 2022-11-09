package ru.practicum.shareit.booking.error;

import javax.persistence.EntityNotFoundException;

public class BookingNotFoundException extends EntityNotFoundException {
    public BookingNotFoundException(Long id) {
        super(String.format("Бронирование с id: %s не найдено.", id));
    }

}
