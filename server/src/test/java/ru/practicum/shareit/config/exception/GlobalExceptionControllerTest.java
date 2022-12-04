package ru.practicum.shareit.config.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.error.*;
import ru.practicum.shareit.config.exception.message.ErrorResponse;
import ru.practicum.shareit.config.exception.message.ValidationErrorResponse;
import ru.practicum.shareit.config.exception.message.Violation;
import ru.practicum.shareit.item.error.CommentIllegalException;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.request.error.ItemRequestNotFoundException;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GlobalExceptionControllerTest {
    @Autowired
    private GlobalExceptionController controller;

    @Test
    @DisplayName("Test errorResponse UserRepeatEmailException")
    void testUserRepeatEmailException() {
        ErrorResponse responseEntity = controller.errorResponse(new UserRepeatEmailException("test"));
        assertNotNull(responseEntity.getError());
        assertNotNull(responseEntity.getMessage());
        assertNotNull(responseEntity);
    }

    @Test
    @DisplayName("Test errorResponse IllegalException")
    void testIllegalException() {
        List<ErrorResponse> responseEntity = List.of(
                controller.errorResponse(new BookingApproveNotAvailable(1L)),
                controller.errorResponse(new ItemNotAvailableException(1L)),
                controller.errorResponse(new CommentIllegalException(1L, 1L))
        );
        assertNotNull(responseEntity);
        assertEquals(3, responseEntity.size());
    }

    @Test
    @DisplayName("Test errorResponse NotFoundException")
    void testNotFoundException() {
        List<ErrorResponse> responseEntity = List.of(
                controller.errorResponse(new ItemNotFoundByUserException(1L, 1L)),
                controller.errorResponse(new UserNotFoundException(1L)),
                controller.errorResponse(new ItemNotFoundException(1L)),
                controller.errorResponse(new BookingNotFoundException(1L)),
                controller.errorResponse(new ItemNotAccessException(1L)),
                controller.errorResponse(new ItemRequestNotFoundException(1L))
        );
        assertNotNull(responseEntity);
        assertEquals(6, responseEntity.size());
    }

    @Test
    @DisplayName("Test errorResponse EntityNotFoundException")
    void testEntityNotFoundException() {
        ErrorResponse responseEntity = controller.errorResponse(new EntityNotFoundException("test"));
        assertNotNull(responseEntity);
    }

    @Test
    @DisplayName("Test errorResponse Throwable")
    void testThrowable() {
        ErrorResponse responseEntity = controller.handleThrowable(new TestAbortedException());
        assertNotNull(responseEntity);
    }

    @Test
    @DisplayName("Test errorResponse UnknownStateException")
    void testUnknownStateException() {
        ErrorResponse responseEntity = controller.handleThrowable(new UnknownStateException("test"));
        assertNotNull(responseEntity);
    }

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