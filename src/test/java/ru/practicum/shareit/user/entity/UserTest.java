package ru.practicum.shareit.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.entity.model.UserDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class UserTest {
    @Autowired
    private JacksonTester<User> json;
    @Autowired
    private JacksonTester<UserDto> jsonDto;

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateUserAndSerializableHim() throws Exception {

        final User user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .build();

        JsonContent<User> result = json.write(user);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@test.test");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateUserDtoDtoAndSerializableHim() throws Exception {

        final UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .build();

        JsonContent<UserDto> result = jsonDto.write(userDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@test.test");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }


    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidUserDtoThenCreated() {

        final UserDto userDtoTest = UserDto.builder()
                .id(1L)
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
                .id(1L)
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
