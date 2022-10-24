package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private long id;
    private long userId;
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime stop;
    private StatusBooking confirm;
}
