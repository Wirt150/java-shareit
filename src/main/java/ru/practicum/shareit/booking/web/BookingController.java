package ru.practicum.shareit.booking.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.entity.model.BookingDtoRequest;
import ru.practicum.shareit.booking.entity.model.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Tag(name = "Контроллер бронирования", description = "Взаимодействие с эндпоинтами сущности Booking")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "Создано"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
                @ApiResponse(responseCode = "404", description = "Неверный id"),
                @ApiResponse(responseCode = "409", description = "Не уникальный email"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @Operation(
            summary = "Создание нового бронирования",
            description = "Создает новое бронирование вещи у определенного пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает новое бронирование",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingDtoRequest create(
            @Valid @RequestBody final BookingDtoResponse dto,
            @Parameter(description = "Booker ID") @RequestHeader(USER_ID_HEADER) final long bookerId
    ) {
        return BookingMapper.toBookingDto(bookingService.add(BookingMapper.toBooking(dto, bookerId), bookerId));
    }

    @Operation(
            summary = "Запрос бронирования по id",
            description = "Возвращает бронирование по id, как для владельца так и для пользователя"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает найденное по id бронирование",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public BookingDtoRequest findById(
            @Parameter(description = "Item ID") @PathVariable final long id,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return BookingMapper.toBookingDto(bookingService.getById(id, userId));
    }

    @Operation(
            summary = "Запрос списка всех бронирований текущего пользователя",
            description = "Выводит список бронирований отсортированных по времени у текущего пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список всех бронирований",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookingDtoRequest> findAll(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long bookerId,
            @Parameter(description = "State Booking") @RequestParam(value = "state", defaultValue = "ALL") final String state
    ) {
        return bookingService.findAll(bookerId, state, from, size).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Operation(
            summary = "Запрос списка всех забронированных вещей текущего пользователя",
            description = "Выводит список забронированных вещей отсортированных по времени у текущего пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список всех бронирований вещей",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public List<BookingDtoRequest> findAllOwner(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long bookerId,
            @Parameter(description = "State Booking") @RequestParam(value = "state", defaultValue = "ALL") final String state
    ) {
        return bookingService.findAllOwner(bookerId, state, from, size).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Operation(
            summary = "Управление статусом бронирования",
            description = "Изменяет статус бронирования на APPROVED, либо REJECTED"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Обновляет статус и возвращает бронирование",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public BookingDtoRequest update(
            @Parameter(description = "Booking ID") @PathVariable final long id,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long owner,
            @Parameter(description = "Status") @RequestParam("approved") final boolean approved
    ) {
        return BookingMapper.toBookingDto(bookingService.approved(id, owner, approved));
    }
}
