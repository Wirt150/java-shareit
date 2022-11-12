package ru.practicum.shareit.booking.error;

public class BookingApproveNotAvailable extends RuntimeException {
    public BookingApproveNotAvailable(Long id) {
        super(String.format("Статус бронирования : %s уже подтвержден.", id));
    }
}
