package ru.practicum.shareit.config.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.config.exception.message.ValidationErrorResponse;
import ru.practicum.shareit.config.exception.message.Violation;

import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GlobalExceptionControllerTest {
    @Autowired
    private GlobalExceptionController controller;

    @Test
    @DisplayName("Test errorResponse onConstraintValidationException")
    void testConstraintValidationException() {
        ValidationErrorResponse responseEntity = controller.onConstraintValidationException(new ConstraintViolationException(Set.of()));
        assertNotNull(responseEntity.getViolations());
        assertNotNull(responseEntity);

        Violation violation = new Violation("test", "test");
        assertNotNull(violation.getErrors());
        assertNotNull(violation.getMessage());
    }
}