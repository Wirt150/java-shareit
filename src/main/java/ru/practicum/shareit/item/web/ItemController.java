package ru.practicum.shareit.item.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.entity.model.CommentDto;
import ru.practicum.shareit.item.entity.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ItemService itemService;

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
    public List<ItemDto> findAll(
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemService.getAll(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
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
    public ItemDto findById(
            @Parameter(description = "Item ID") @PathVariable final long id,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return ItemMapper.toItemDto(itemService.getById(id, userId));
    }

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
    public ItemDto create(
            @Valid @RequestBody final ItemDto dto,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return ItemMapper.toItemDto(itemService.add(ItemMapper.toItem(dto, userId), userId));
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
    public ItemDto update(
            @RequestBody final ItemDto dto,
            @Parameter(description = "Item ID") @PathVariable final long id,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return ItemMapper.toItemDto(itemService.update(ItemMapper.toItem(dto, userId), id, userId));
    }

    @Operation(
            summary = "Поиск по подстроке в описании или названии всех вещей",
            description = "Ищет в базе соответствие поданной в параметр подстроки и возращает все совпадения"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Возвращает список найденных вещей",
            content = {
                    @Content(mediaType = "application/json")
            })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemDto> search(
            @Parameter(description = "Substring for search") @RequestParam("text") final String text,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemService.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
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
    public CommentDto addComment(
            @Valid @RequestBody final CommentDto dto,
            @Parameter(description = "User ID") @RequestHeader(USER_ID_HEADER) final long author,
            @Parameter(description = "Item ID") @PathVariable long itemId
    ) {
        return CommentMapper.toCommentDto(itemService.addComment(CommentMapper.toComment(dto, author, itemId), author, itemId));
    }


}
