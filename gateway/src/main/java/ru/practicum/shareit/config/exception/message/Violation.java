package ru.practicum.shareit.config.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Violation {
    private final String errors;
    private final String message;
}