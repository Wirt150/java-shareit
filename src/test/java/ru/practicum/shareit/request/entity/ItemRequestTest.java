package ru.practicum.shareit.request.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.ItemDtoRequestInfo;
import ru.practicum.shareit.request.entity.model.ItemRequestDto;
import ru.practicum.shareit.user.entity.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemRequestTest {
    @Autowired
    private JacksonTester<ItemRequest> json;
    @Autowired
    private JacksonTester<ItemRequestDto> jsonDto;
    private final Timestamp timestamp = Timestamp.valueOf("2022-10-10 10:10:00");
    private final User user = User.builder()
            .id(1L)
            .email("test@test.test")
            .name("Name")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("TestName")
            .description("testDescription")
            .owner(user)
            .available(true)
            .build();

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateItemRequestAndSerializableHim() throws Exception {

        final ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .author(user)
                .description("test")
                .created(timestamp)
                .items(List.of(item))
                .build();

        JsonContent<ItemRequest> result = json.write(itemRequest);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.author").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.author").extracting("name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathValue("$.author").extracting("email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-10-10T07:10:00.000+00:00");
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("available").isEqualTo(item.getAvailable());
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateItemRequestDtoAndSerializableHim() throws Exception {

        final ItemDtoRequestInfo itemDtoRequestInfo = ItemDtoRequestInfo.builder()
                .id(1L)
                .name("TestName")
                .description("testDescription")
                .available(true)
                .requestId(user.getId())
                .build();

        final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .created(timestamp.toLocalDateTime())
                .items(List.of(itemDtoRequestInfo))
                .build();

        JsonContent<ItemRequestDto> result = jsonDto.write(itemRequestDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2022-10-10T10:10:00");
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathValue("$.items[0]").extracting("available").isEqualTo(item.getAvailable());
    }

    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidItemRequestDtoThenCreated() {

        final ItemRequestDto itemRequestDtoTest = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.now())
                .items(List.of(ItemDtoRequestInfo.builder().build()))
                .build();

        //test
        assertEquals(0, testingValidator(itemRequestDtoTest).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Создаем проверяемый объект с ошибками валидации.")
    void whenCreateValidItemRequestDtoThenNotCreated() {

        final ItemRequestDto itemRequestDtoTest = ItemRequestDto.builder()
                .id(1L)
                .description("")
                .created(LocalDateTime.now())
                .items(List.of(ItemDtoRequestInfo.builder().build()))
                .build();

        //test
        assertEquals(1, testingValidator(itemRequestDtoTest).size(), "Размер списка должен быть равен 1.");
    }

    private Set<ConstraintViolation<ItemRequestDto>> testingValidator(ItemRequestDto itemRequestDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(itemRequestDto);
    }
}