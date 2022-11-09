package ru.practicum.shareit.user.web;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.entity.model.UserDto;

@UtilityClass
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail()).build();
    }
}
