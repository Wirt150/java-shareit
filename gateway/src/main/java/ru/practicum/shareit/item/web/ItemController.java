package ru.practicum.shareit.item.web;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Контроллер вещей", description = "Взаимодействие с эндпоинтами сущности Item")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "Создано"),
                @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
                @ApiResponse(responseCode = "404", description = "Неверный id"),
                @ApiResponse(responseCode = "409", description = "Не уникальный email"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @Operation(
            summary = "Создание новой вещи",
            description = "Создает новую вещь у определенного пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданную вещь",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(
            @Valid @RequestBody final ItemDto dto,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemClient.create(dto, userId);
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
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemClient.findById(id, userId);
    }

    @Operation(
            summary = "Запрос списка всех вещей определенного пользователя",
            description = "Выводит список пользователей отсортированных по id у определенного пользователя (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список всех вещей",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> findAll(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemClient.findAll(from, size, userId);
    }

    @Operation(
            summary = "Обновление полей существующей вещи у существующего пользователя",
            description = "Обновляет только поданные в RequestBody поля у сущности в базе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Обновляет вещь и возвращает её",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(
            @RequestBody final ItemDto dto,
            @Parameter(description = "Item ID") @PathVariable final long id,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemClient.update(id, dto, userId);
    }

    @Operation(
            summary = "Поиск по подстроке в описании или названии всех вещей",
            description = "Ищет в базе соответствие поданной в параметр подстроки и возвращает все совпадения"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список найденных вещей",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") final int from,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") final int size,
            @Parameter(description = "Substring for search") @RequestParam("text") final String text,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemClient.search(text, from, size, userId);
    }

    @Operation(
            summary = "Добавление комментария к вещи",
            description = "Создает новый комментария у определенной вещи (id)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданный комментарий",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @Valid @RequestBody final CommentDto dto,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long author,
            @Parameter(description = "Item ID") @PathVariable long itemId
    ) {
        return itemClient.addComment(dto, author, itemId);
    }
}
