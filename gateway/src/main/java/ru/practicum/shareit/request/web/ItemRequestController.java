package ru.practicum.shareit.request.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Tag(name = "Контроллер реквестов", description = "Взаимодействие с эндпоинтами сущности ItemRequest")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "Создано"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
                @ApiResponse(responseCode = "404", description = "Неверный id"),
                @ApiResponse(responseCode = "409", description = "Не уникальный email"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @Operation(
            summary = "Создание нового запроса",
            description = "Создает новый запрос у определенного пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданный запрос",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<Object> create(
            @Valid @RequestBody final ItemRequestDto dto,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long authorId
    ) {
        return itemRequestClient.create(dto, authorId);
    }

    @Operation(
            summary = "Запрос вещи по id",
            description = "Возвращает вещь по id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает найденную вещь по id",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> findById(
            @Parameter(description = "Item ID") @PathVariable final long id,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long authorId
    ) {
        return itemRequestClient.findById(id, authorId);
    }

    @Operation(
            summary = "Запрос списка всех запросов определенного пользователя",
            description = "Выводит список всех запросов с ответами отсортированных по времени у определенного пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список всех запросов с ответами",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> findAll(
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemRequestClient.findAll(userId);
    }

    @Operation(
            summary = "Запрос списка всех запросов определенного пользователя",
            description = "Выводит список всех запросов с ответами отсортированных по времени у определенного пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список всех запросов с ответами",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("all")
    public ResponseEntity<Object> findAllPageable(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemRequestClient.findAllPageable(from, size, userId);
    }
}