package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.entity.model.UserDto;
import ru.practicum.shareit.user.web.UserController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class UserIntegrationTest {
    private final UserDto userDtoTestOne = UserDto.builder()
            .id(0L)
            .name("TestName1")
            .email("test1@test.test")
            .build();
    private final UserDto userDtoTestTwo = UserDto.builder()
            .id(0L)
            .name("TestName2")
            .email("test2@test.test")
            .build();
    @Autowired
    private UserController userController;

    @Test
    @DisplayName("Проверяем метод GET(все id) контроллера user.")
    void whenCheckGetAllIdMethod() {
        UserDto userDtoOne = userController.create(userDtoTestOne);
        UserDto userDtoTwo = userController.create(userDtoTestTwo);

        //test
        List<UserDto> userDtos = userController.findAll();
        assertEquals(2, userDtos.size(), "Размер списка должен быть равен 2.");
        assertEquals(userDtoOne, userDtos.get(0), "Пользователи должны совпадать.");
        assertEquals(userDtoTwo, userDtos.get(1), "Пользователи должны совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод GET(id) контроллера user.")
    void whenCheckGetIdMethod() {
        UserDto userDtoOne = userController.create(userDtoTestOne);
        UserDto userDtoTwo = userController.create(userDtoTestTwo);

        //test
        assertEquals(userDtoOne, userController.findById(1), "Пользователи должны совпадать.");
        assertNotEquals(userDtoTwo, userController.findById(1), "Пользователи не должны совпадать");
        assertEquals(userDtoTwo, userController.findById(2), "Пользователи должны совпадать.");
        assertNotEquals(userDtoOne, userController.findById(2), "Пользователи не должны совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод POST контроллера user.")
    void whenCheckCreateMethod() {
        UserDto userDtoOne = userController.create(userDtoTestOne);
        UserDto userDtoTwo = userController.create(userDtoTestTwo);

        //test
        assertEquals(userDtoOne, userController.findById(1), "Пользователи должны совпадать.");
        assertEquals(1, userDtoOne.getId(), "Id должен быть равен 1.");
        assertEquals("TestName1", userDtoOne.getName(), "Имя должно совпадать.");
        assertEquals("test1@test.test", userDtoOne.getEmail(), "Почта должна совпадать.");
        assertEquals(userDtoTwo, userController.findById(2), "Пользователи должны совпадать.");
        assertEquals("TestName2", userDtoTwo.getName(), "Имя должно совпадать.");
        assertEquals("test2@test.test", userDtoTwo.getEmail(), "Почта должна совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод PATCH контроллера user.")
    void whenCheckUpdateMethod() {
        UserDto userDto = userController.create(userDtoTestOne);
        UserDto updateName = UserDto.builder().name("update").build();
        UserDto updateEmail = UserDto.builder().email("update@update.com").build();

        userController.update(updateName, 1);

        //test
        UserDto updateNameTest = userController.findById(1);
        assertNotEquals(userDto, updateNameTest, "Пользователи не должны совпадать.");
        assertEquals(1, updateNameTest.getId(), "Id должен быть равен 1.");
        assertEquals("update", updateNameTest.getName(), "Имя должно совпадать.");

        userController.update(updateEmail, 1);

        //test
        UserDto updateEmailTest = userController.findById(1);
        assertNotEquals(userDto, updateNameTest, "Пользователи не должны совпадать.");
        assertEquals(1, updateNameTest.getId(), "Id должен быть равен 1.");
        assertEquals("update@update.com", updateEmailTest.getEmail(), "Email должен совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод DELETE контроллера user.")
    void whenCheckDeleteMethod() {
        userController.create(userDtoTestOne);
        UserDto userDto = userController.create(userDtoTestTwo);
        userController.delete(1);

        List<UserDto> userDtosOne = userController.findAll();

        //test
        assertEquals(1, userDtosOne.size(), "Размер списка должен быть равен 1.");
        assertEquals(userDto, userDtosOne.get(0), "Пользователи должны совпадать.");

        userController.delete(2);
        List<UserDto> userDtosThree = userController.findAll();

        //test
        assertEquals(0, userDtosThree.size(), "Размер списка должен быть равен 0.");
    }
}