package ru.practicum.shareit.config.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorBookingResponse {
    private final String errorMessage;
}
