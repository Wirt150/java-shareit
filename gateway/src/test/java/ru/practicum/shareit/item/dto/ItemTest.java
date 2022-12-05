package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemTest {

    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidItemDtoThenCreated() {

        final ItemDto itemDtoTest = ItemDto.builder()
                .id(0L)
                .name("TestName1")
                .description("testDescription1")
                .owner(1L)
                .available(true)
                .build();

        //test
        assertEquals(0, testingValidator(itemDtoTest).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Создаем проверяемый объект с ошибками валидации.")
    void whenCreateValidItemDtoThenNotCreated() {

        final ItemDto itemDtoTest = ItemDto.builder()
                .id(0L)
                .name("")
                .description("")
                .owner(1L)
                .available(null)
                .build();

        Set<ConstraintViolation<ItemDto>> errors = testingValidator(itemDtoTest);
        errors.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

        //test
        assertEquals(3, errors.size(), "Размер списка должен быть равен 3.");
    }

    private Set<ConstraintViolation<ItemDto>> testingValidator(ItemDto itemDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(itemDto);
    }
}
