package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingTest {

    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidBookingDtoThenCreated() {

        final BookingDtoResponse bookingDtoResponse = BookingDtoResponse.builder()
                .itemId(1)
                .start(LocalDateTime.of(2024, 1, 1, 1, 1))
                .end(LocalDateTime.of(2025, 1, 1, 1, 1))
                .build();

        //test
        assertEquals(0, testingValidator(bookingDtoResponse).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Создаем проверяемый объект с ошибками валидации.")
    void whenCreateValidBookingDtoThenNotCreated() {

        BookingDtoResponse bookingDtoResponse = BookingDtoResponse.builder()
                .itemId(1)
                .start(LocalDateTime.of(2000, 1, 1, 1, 1))
                .end(LocalDateTime.of(2001, 1, 1, 1, 1))
                .build();

        Set<ConstraintViolation<BookingDtoResponse>> errors2 = testingValidator(bookingDtoResponse);
        errors2.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

        //test
        assertEquals(2, errors2.size(), "Размер списка должен быть равен 2.");

        bookingDtoResponse = BookingDtoResponse.builder()
                .itemId(1)
                .start(LocalDateTime.of(2024, 1, 1, 1, 1))
                .end(LocalDateTime.of(2023, 1, 1, 1, 1))
                .build();

        Set<ConstraintViolation<BookingDtoResponse>> errors1 = testingValidator(bookingDtoResponse);
        errors1.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

        assertEquals(1, errors1.size(), "Размер списка должен быть равен 1.");

    }

    private Set<ConstraintViolation<BookingDtoResponse>> testingValidator(BookingDtoResponse bookingDtoResponse) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(bookingDtoResponse);
    }
}
