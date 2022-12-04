package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.error.UserRepeatEmailException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository mockUserRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .build();
        reset(mockUserRepository);
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("Проверяем метод add сервиса User.")
    void whenCheckAddMethod() {
        when(mockUserRepository.save(user)).thenReturn(user);

        //test
        User testUser = userService.add(user);
        assertEquals(1L, testUser.getId());
        assertEquals("test", testUser.getName());
        assertEquals("test@test.test", testUser.getEmail());

        verify(mockUserRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Проверяем метод getById сервиса User.")
    void whenCheckoutGetByIdMethod() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        //test
        User testUser = userService.getById(1L);
        assertEquals(1L, testUser.getId());
        assertEquals("test", testUser.getName());
        assertEquals("test@test.test", testUser.getEmail());

        verify(mockUserRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Проверяем метод getAll сервиса User.")
    void whenCheckoutGetAllMethod() {
        when(mockUserRepository.findAll()).thenReturn(List.of(user));

        //test
        List<User> users = userService.getAll();
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));

        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Проверяем метод update сервиса User")
    void whenCheckoutUpdateMethod() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mockUserRepository.save(user)).thenReturn(user);

        //test
        User testUser = userService.update(user, 1L);
        assertEquals(1L, testUser.getId());
        assertEquals("test", testUser.getName());
        assertEquals("test@test.test", testUser.getEmail());

        verify(mockUserRepository, times(1)).findById(1L);
        verify(mockUserRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Проверяем метод delete сервиса User")
    void whenCheckoutDeleteMethod() {
        //test
        userService.delete(1L);

        verify(mockUserRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("Ищем несуществующий Id и ловим UserNotFoundException")
    void testFindByIdNotFound() {
        final RuntimeException exceptionId = assertThrows(
                UserNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.getById(2L);
                    }
                });

        //test
        assertEquals(exceptionId.getMessage(), String.format(String.format("Пользователь с id: %s не найден.", 2)),
                "При несуществующем id должна выброситься ошибка");

        verify(mockUserRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Дублируем email и ловим UserRepeatEmailException")
    void testAddUserRepeatEmailException() {
        when(mockUserRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        final RuntimeException exceptionEmail = assertThrows(
                UserRepeatEmailException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        userService.add(user);
                    }
                });

        //test
        assertEquals(exceptionEmail.getMessage(), String.format("Пользователь c таким email: %s существует.", user.getEmail()),
                "При одинаковых email должна выброситься ошибка");

        verify(mockUserRepository, times(1)).save(user);
    }
}

