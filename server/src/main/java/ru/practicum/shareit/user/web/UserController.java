package ru.practicum.shareit.user.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.entity.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto create(@RequestBody final UserDto dto) {
        return UserMapper.toUserDto(userService.add(UserMapper.toUser(dto)));
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public UserDto findById(@PathVariable final long id) {
        return UserMapper.toUserDto(userService.getById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<UserDto> findAll() {
        return userService.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }


    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public UserDto update(
            @RequestBody final UserDto dto,
            @PathVariable final long id) {
        return UserMapper.toUserDto(userService.update(UserMapper.toUser(dto), id));
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable final long id) {
        userService.delete(id);
    }
}

