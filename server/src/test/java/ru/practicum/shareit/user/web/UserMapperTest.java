package ru.practicum.shareit.user.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.entity.model.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class UserMapperTest {

    private final User userTest = User.builder()
            .id(1L)
            .email("test@test.test")
            .name("test")
            .build();

    @Test
    @DisplayName("Проверяем методы маппера User.")
    void whenCheckUserMapper() {
        final UserDto userDto = UserMapper.toUserDto(userTest);

        //test
        assertThat(userDto, notNullValue());
        assertThat(userDto.getId(), equalTo(userTest.getId()));
        assertThat(userDto.getName(), equalTo(userTest.getName()));
        assertThat(userDto.getEmail(), equalTo(userTest.getEmail()));

        final User user = UserMapper.toUser(userDto);

        //test
        assertThat(user, notNullValue());
        assertThat(user.getId(), equalTo(userTest.getId()));
        assertThat(user.getName(), equalTo(userTest.getName()));
        assertThat(user.getEmail(), equalTo(userTest.getEmail()));
    }
}
