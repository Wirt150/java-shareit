package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTest {

    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidUserDtoThenCreated() {

        final UserDto userDtoTest = UserDto.builder().id(0L)
                .email("test@test.test")
                .name("Name")
                .build();

        //test
        assertEquals(0, testingValidator(userDtoTest).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Создаем проверяемый объект с ошибками валидации.")
    void whenCreateValidUserDtoThenNotCreated() {

        final UserDto userDtoTest = UserDto.builder()
                .id(0L)
                .email("")
                .name("Name")
                .build();

        Set<ConstraintViolation<UserDto>> errors = testingValidator(userDtoTest);
        errors.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

        //test
        assertEquals(2, errors.size(), "Размер списка должен быть равен 2.");
    }

    private Set<ConstraintViolation<UserDto>> testingValidator(UserDto userDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(userDto);
    }
}
