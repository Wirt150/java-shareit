package ru.practicum.shareit.item.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.web.UserController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {

    private static final long USER_ID = 1L;
    @Autowired
    private ItemController itemController;
    @Autowired
    private UserController userController;

    private final ItemDto itemDtoTestOne = ItemDto.builder()
            .id(0L)
            .name("TestName1")
            .description("testDescription1")
            .userId(1L)
            .available(true)
            .build();

    private final ItemDto itemDtoTestTwo = ItemDto.builder()
            .id(0L)
            .name("TestName2")
            .description("testDescription2")
            .userId(1L)
            .available(true)
            .build();

    private UserDto userDtoTest = UserDto.builder()
            .id(0L)
            .name("TestName")
            .email("test@test.test")
            .build();

    @BeforeEach
    void setUp() {
        userDtoTest = userController.create(userDtoTest).get();
    }

    @Test
    @DisplayName("Проверяем метод GET(все id) контроллера item.")
    void whenCheckGetAllIdMethod() {
        ItemDto itemDtoOne = itemController.create(itemDtoTestOne, USER_ID).get();
        ItemDto itemDtoTwo = itemController.create(itemDtoTestTwo, USER_ID).get();

        //test
        List<ItemDto> itemDtos = itemController.findAll(USER_ID);
        assertEquals(2, itemDtos.size(), "Размер списка должен быть равен 2.");
        assertEquals(itemDtoOne, itemDtos.get(0), "Вещи должны совпадать.");
        assertEquals(itemDtoTwo, itemDtos.get(1), "Вещи должны совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод GET(id) контроллера item.")
    void whenCheckGetIdMethod() {
        ItemDto itemDtoOne = itemController.create(itemDtoTestOne, USER_ID).get();
        ItemDto itemDtoTwo = itemController.create(itemDtoTestTwo, USER_ID).get();

        //test
        assertEquals(itemDtoOne, itemController.findById(1, USER_ID).get(), "Вещи должны совпадать.");
        assertNotEquals(itemDtoTwo, itemController.findById(1, USER_ID).get(), "Вещи не должны совпадать");
        assertEquals(itemDtoTwo, itemController.findById(2, USER_ID).get(), "Вещи должны совпадать.");
        assertNotEquals(itemDtoOne, itemController.findById(2, USER_ID).get(), "Вещи не должны совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод POST контроллера item.")
    void whenCheckCreateMethod() {
        ItemDto itemDtoOne = itemController.create(itemDtoTestOne, USER_ID).get();
        ItemDto itemDtoTwo = itemController.create(itemDtoTestTwo, USER_ID).get();

        //test
        assertEquals(itemDtoOne, itemController.findById(1, USER_ID).get(), "Вещи должны совпадать.");
        assertEquals(1, itemDtoOne.getId(), "Id должен быть равен 1.");
        assertEquals("TestName1", itemDtoOne.getName(), "Имя должно совпадать.");
        assertEquals("testDescription1", itemDtoOne.getDescription(), "Описание должно совпадать.");
        assertEquals(userDtoTest.getId(), itemDtoOne.getUserId(), "Id пользователя должно совпадать.");
        assertTrue(itemDtoOne.getAvailable(), "Доступность должна совпадать.");

        assertEquals(itemDtoTwo, itemController.findById(2, USER_ID).get(), "Вещи должны совпадать.");
        assertEquals(2, itemDtoTwo.getId(), "Id должен быть равен 1.");
        assertEquals("TestName2", itemDtoTwo.getName(), "Имя должно совпадать.");
        assertEquals("testDescription2", itemDtoTwo.getDescription(), "Описание должно совпадать.");
        assertEquals(userDtoTest.getId(), itemDtoTwo.getUserId(), "Id пользователя должно совпадать.");
        assertTrue(itemDtoTwo.getAvailable(), "Доступность должна совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод PATCH контроллера item.")
    void whenCheckUpdateMethod() {
        ItemDto itemDto = itemController.create(itemDtoTestOne, USER_ID).get();
        ItemDto updateName = ItemDto.builder().name("update").build();
        ItemDto updateDescription = ItemDto.builder().description("update").build();
        ItemDto updateAvailable = ItemDto.builder().available(false).build();

        itemController.update(updateName, 1, USER_ID);

        //test
        ItemDto updateNameTest = itemController.findById(1, USER_ID).get();
        assertNotEquals(itemDto, updateNameTest, "Пользователи не должны совпадать.");
        assertEquals(1, updateNameTest.getId(), "Id должен быть равен 1.");
        assertEquals("update", updateNameTest.getName(), "Имя должно совпадать.");

        itemController.update(updateDescription, 1, USER_ID);

        //test
        ItemDto updateEmailTest = itemController.findById(1, USER_ID).get();
        assertNotEquals(itemDto, updateNameTest, "Пользователи не должны совпадать.");
        assertEquals(1, updateNameTest.getId(), "Id должен быть равен 1.");
        assertEquals("update", updateEmailTest.getDescription(), "Описание должно совпадать.");

        itemController.update(updateAvailable, 1, USER_ID);

        //test
        ItemDto updateAvailableTest = itemController.findById(1, USER_ID).get();
        assertNotEquals(itemDto, updateNameTest, "Пользователи не должны совпадать.");
        assertEquals(1, updateNameTest.getId(), "Id должен быть равен 1.");
        assertFalse(updateAvailableTest.getAvailable(), "Доступность должна быть false.");
    }

    @Test
    @DisplayName("Проверяем метод GET(search) контроллера item.")
    void search() {
        ItemDto itemDtoOne = itemController.create(itemDtoTestOne, USER_ID).get();
        List<ItemDto> dtosOne = itemController.search("TeS", USER_ID);

        //test
        assertEquals(1, dtosOne.size(), "Размер списка должен равняться 1.");
        assertEquals(itemDtoOne, dtosOne.get(0), "Размер списка должен равняться 1.");

        ItemDto itemDtoTwo = itemController.create(itemDtoTestTwo, USER_ID).get();
        List<ItemDto> dtosTwo = itemController.search("Tes", USER_ID);

        //test
        assertEquals(2, dtosTwo.size(), "Размер списка должен равняться 2.");
        assertEquals(itemDtoOne, dtosOne.get(0), "Размер списка должен равняться 1.");
        assertEquals(itemDtoTwo, dtosTwo.get(1), "Размер списка должен равняться 1.");
    }
}