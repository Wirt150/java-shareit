package ru.practicum.shareit.user.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.entity.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    private final UserService userService;

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
    public List<UserDto> findAll() {

        return userService.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
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
    public UserDto findById(@PathVariable @Parameter(description = "id пользователя") final long id) {
        return UserMapper.toUserDto(userService.getById(id));
    }

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
    public UserDto create(@Valid @RequestBody final UserDto dto) {
        return UserMapper.toUserDto(userService.add(UserMapper.toUser(dto)));
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
    public UserDto update(
            @RequestBody final UserDto dto,
            @PathVariable @Parameter(description = "id пользователя") final long id) {
        return UserMapper.toUserDto(userService.update(UserMapper.toUser(dto), id));
    }

    @Operation(summary = "Удаление пользователя по его id")
    @ApiResponse(
            responseCode = "200",
            description = "Удаляет пользователя и ничего не возвращает"
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void delete(@PathVariable @Parameter(description = "id пользователя") final long id) {
        userService.delete(id);
    }
}

