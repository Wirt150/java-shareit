package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestTest {
    private final Timestamp timestamp = Timestamp.valueOf("2022-10-10 10:10:00");


    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidItemRequestDtoThenCreated() {

        final ItemRequestDto itemRequestDtoTest = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.now())
                .items(List.of())
                .build();

        //test
        assertEquals(0, testingValidator(itemRequestDtoTest).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Создаем проверяемый объект с ошибками валидации.")
    void whenCreateValidItemRequestDtoThenNotCreated() {

        final ItemRequestDto itemRequestDtoTest = ItemRequestDto.builder()
                .id(1L)
                .description("")
                .created(LocalDateTime.now())
                .items(List.of())
                .build();

        //test
        assertEquals(1, testingValidator(itemRequestDtoTest).size(), "Размер списка должен быть равен 1.");
    }

    private Set<ConstraintViolation<ItemRequestDto>> testingValidator(ItemRequestDto itemRequestDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(itemRequestDto);
    }
}