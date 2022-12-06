package ru.practicum.shareit.request.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.entity.model.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ItemRequestDto create(
            @RequestBody final ItemRequestDto dto,
            @RequestHeader(USER_ID_HEADER) final long authorId
    ) {
        return ItemRequestMapper.toItemRequestDto(itemRequestService.add(ItemRequestMapper.toItemRequest(dto, authorId), authorId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ItemRequestDto findById(
            @PathVariable final long id,
            @RequestHeader(USER_ID_HEADER) final long authorId
    ) {
        return ItemRequestMapper.toItemRequestDto(itemRequestService.getById(id, authorId));
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ItemRequestDto> findAll(
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemRequestService.getAll(userId).stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("all")
    public List<ItemRequestDto> findAllPageable(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return itemRequestService.getPage(from, size, userId).stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }
}