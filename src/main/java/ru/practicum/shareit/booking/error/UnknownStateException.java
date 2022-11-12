package ru.practicum.shareit.booking.error;

public class UnknownStateException extends RuntimeException {
    public UnknownStateException(String state) {
        super(state);
    }
}
