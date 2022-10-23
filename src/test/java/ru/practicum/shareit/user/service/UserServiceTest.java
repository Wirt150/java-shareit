package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserService userService;
    private final User userTest = User.builder()
            .id(0L)
            .email("email@email.com")
            .name("Name").build();

    @Test
    @DisplayName("Проверяем ошибки сервисного слоя пользователей.")
    void whenCheckUserServiceForException() {
        userService.addDto(userTest);

        final RuntimeException exceptionEmail = assertThrows(
                UserRepeatEmailException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.addDto(userTest);
                    }
                });

        //test
        assertEquals(exceptionEmail.getMessage(), String.format("Пользователь c таким email: %s существует.", userTest.getEmail()),
                "При одинаковых email должна выброситься ошибка");


        final RuntimeException exceptionId = assertThrows(
                UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.getDtoById(2);
                    }
                });

        //test
        assertEquals(exceptionId.getMessage(), String.format(String.format("Пользователь с id:%s не найден.", 2)),
                "При несуществующем id должна выброситься ошибка");
    }
}