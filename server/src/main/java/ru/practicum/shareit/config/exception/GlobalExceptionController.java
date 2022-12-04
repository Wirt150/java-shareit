package ru.practicum.shareit.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
import java.util.stream.Collectors;

@RestControllerAdvice("ru.practicum.shareit")
public class GlobalExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse errorResponse(final UserRepeatEmailException e) {
        return new ErrorResponse("Ошибка поданных данных:", e.getMessage());
    }

    @ExceptionHandler(value = {
            BookingApproveNotAvailable.class,
            ItemNotAvailableException.class,
            CommentIllegalException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse errorResponse(final RuntimeException e) {
        return new ErrorResponse("Ошибка поданных данных:", e.getMessage());
    }

    @ExceptionHandler(value = {
            ItemNotFoundByUserException.class,
            UserNotFoundException.class,
            ItemNotFoundException.class,
            BookingNotFoundException.class,
            ItemNotAccessException.class,
            ItemRequestNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse errorResponse(final EntityNotFoundException e) {
        return new ErrorResponse("Ошибка поданных данных:", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse("Непредвиденная ошибка.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final UnknownStateException e) {
        return new ErrorResponse("Unknown state: " + e.getMessage(), "Неверный статус операции");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onConstraintValidationException(final ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}




