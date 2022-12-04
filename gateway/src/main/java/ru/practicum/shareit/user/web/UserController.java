package ru.practicum.shareit.user.web;

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
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователей", description = "Взаимодействие с эндпоинтами сущности User")
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "201", description = "Создано"),
                @ApiResponse(responseCode = "404", description = "Неверный id"),
                @ApiResponse(responseCode = "409", description = "Не уникальный email"),
                @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка"),
        }
)
public class UserController {

    private final UserClient userClient;

    @Operation(
            summary = "Создание нового пользователя",
            description = "Создает нового пользователя и присваивает id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает созданного пользователя",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody final UserDto dto) {
        return userClient.create(dto);
    }

    @Operation(
            summary = "Запрос пользователя по id",
            description = "Возвращает пользователя по id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает найденного пользователя по id",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ResponseEntity<Object> findById(@PathVariable @Parameter(description = "id пользователя") final long id) {
        return userClient.findById(id);
    }

    @Operation(
            summary = "Запрос списка всех пользователей",
            description = "Выводит список пользователей отсортированных по id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список всех пользователей",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @Operation(
            summary = "Обновление полей существующего пользователя",
            description = "Обновляет только поданные в RequestBody поля у сущности в базе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Обновляет пользователя и возвращает его",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ResponseEntity<Object> update(
            @RequestBody final UserDto dto,
            @PathVariable @Parameter(description = "id пользователя") final long id) {
        return userClient.update(id, dto);
    }

    @Operation(summary = "Удаление пользователя по его id")
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет пользователя и ничего не возвращает"
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void delete(@PathVariable @Parameter(description = "id пользователя") final long id) {
        userClient.delete(id);
    }
}

