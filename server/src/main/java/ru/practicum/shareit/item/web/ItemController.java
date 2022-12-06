package ru.practicum.shareit.item.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.entity.model.CommentDto;
import ru.practicum.shareit.item.entity.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ItemDto create(
            @RequestBody final ItemDto dto,
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return ItemMapper.toItemDto(itemService.add(ItemMapper.toItem(dto, userId), userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ItemDto findById(
            @PathVariable final long id,
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return ItemMapper.toItemDto(itemService.getById(id, userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemDto> findAll(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemService.getAll(userId, from, size).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public ItemDto update(
            @RequestBody final ItemDto dto,
            @PathVariable final long id,
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return ItemMapper.toItemDto(itemService.update(ItemMapper.toItem(dto, userId), id, userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<ItemDto> search(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @RequestParam("text") final String text,
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemService.search(text, from, size).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("{itemId}/comment")
    public CommentDto addComment(
            @RequestBody final CommentDto dto,
            @RequestHeader(USER_ID_HEADER) final long author,
            @PathVariable long itemId
    ) {
        return CommentMapper.toCommentDto(itemService.addComment(CommentMapper.toComment(dto, author, itemId), author, itemId));
    }


}
