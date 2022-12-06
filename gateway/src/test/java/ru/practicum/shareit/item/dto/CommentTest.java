package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Timestamp;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentTest {

    private final Timestamp timestamp = Timestamp.valueOf("2022-10-10 10:10:00");

    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidCommentDtoThenCreated() {

        final CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("test")
                .authorName("Name")
                .created(timestamp)
                .build();

        //test
        assertEquals(0, testingValidator(commentDto).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Создаем проверяемый объект с ошибками валидации.")
    void whenCreateValidCommentDtoThenNotCreated() {

        final CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("")
                .authorName("Name")
                .created(timestamp)
                .build();

        Set<ConstraintViolation<CommentDto>> errors = testingValidator(commentDto);
        errors.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

        //test
        assertEquals(1, errors.size(), "Размер списка должен быть равен 1.");
    }

    private Set<ConstraintViolation<CommentDto>> testingValidator(CommentDto commentDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(commentDto);
    }
}
