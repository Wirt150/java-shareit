package ru.practicum.shareit.booking.entity.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;
import ru.practicum.shareit.config.validator.ValidateDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@ValidateDate(message = "Дата окончания бронирования не может быть раньше даты начала бронирования.")
public class BookingDtoResponse implements Serializable {
    private final long itemId;
    @NonNull
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    private final LocalDateTime start;
    @NonNull
    @Future(message = "Дата конца бронирования не может быть в прошлом")
    private final LocalDateTime end;
}
