package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceTest {

    private static final long USER_ID = 1L;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;


    private final Item itemTest = Item.builder()
            .id(0L)
            .name("TestName1")
            .description("testDescription1")
            .userId(1L)
            .available(true)
            .build();

    private final User userTest = User.builder()
            .id(0L)
            .email("email@email.com")
            .name("Name")
            .build();

    @Test
    @DisplayName("Проверяем ошибки сервисного слоя вещей.")
    void whenCheckItemServiceForException() {
        userService.addDto(userTest);
        itemService.addDto(itemTest, USER_ID);

        final RuntimeException exceptionItemByUser = assertThrows(
                ItemNotFoundByUserException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.updateDto(itemTest, 2, USER_ID);
                    }
                });

        //test
        assertEquals(exceptionItemByUser.getMessage(), String.format("Вещь с id:%s у пользователя с id:%s не найдена.", 2, USER_ID),
                "У пользователя не должно быть такой вещи.");

        final RuntimeException exceptionId = assertThrows(
                ItemNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.getDtoById(2);
                    }
                });

        //test
        assertEquals(exceptionId.getMessage(), String.format("Вещь с id:%s не найдена.", 2),
                "При несуществующем id должна выброситься ошибка");
    }
}