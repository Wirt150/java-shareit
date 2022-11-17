package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.error.CommentIllegalException;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
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
            .owner(User.builder().id(1L).build())
            .available(true)
            .build();

    private final User userTest = User.builder()
            .id(0L)
            .email("email@email.com")
            .name("Name")
            .build();

    private final Comment commentTest = Comment.builder()
            .id(0L)
            .text("test")
            .author(userTest)
            .created(Timestamp.from(Instant.now()))
            .build();

    @Test
    @DisplayName("Проверяем ошибки сервисного слоя вещей.")
    void whenCheckItemServiceForException() {
        userService.add(userTest);
        itemService.add(itemTest, USER_ID);

        final RuntimeException exceptionItemByUser = assertThrows(
                ItemNotFoundByUserException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.update(itemTest, 1, USER_ID + 1);
                    }
                });

        //test
        assertEquals(exceptionItemByUser.getMessage(), String.format("Вещь с id: %s у пользователя с id: %s не найдена.", 1, USER_ID + 1),
                "У пользователя не должно быть такой вещи.");

        final RuntimeException exceptionId = assertThrows(
                ItemNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.getById(2, USER_ID);
                    }
                });

        //test
        assertEquals(exceptionId.getMessage(), String.format("Вещь с id: %s не найдена.", 2),
                "При несуществующем id должна выброситься ошибка");

        final RuntimeException exceptionComment = assertThrows(
                CommentIllegalException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.addComment(commentTest, 1, 2);
                    }
                });

        assertEquals(exceptionComment.getMessage(), String.format("Пользователь с id: %s не может оставить комментарий для вещи id: %s.", 1, 2),
                "Пользователь не бронировавший вещь не может ее комментировать");
    }
}