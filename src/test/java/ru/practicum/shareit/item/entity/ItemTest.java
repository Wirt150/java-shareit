package ru.practicum.shareit.item.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.entity.model.ItemDto;
import ru.practicum.shareit.item.entity.model.ItemDtoBookingInfo;
import ru.practicum.shareit.item.entity.model.ItemDtoBookingResponse;
import ru.practicum.shareit.item.entity.model.ItemDtoRequestInfo;
import ru.practicum.shareit.user.entity.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemTest {

    @Autowired
    private JacksonTester<Item> json;
    @Autowired
    private JacksonTester<ItemDto> jsonDto;
    @Autowired
    private JacksonTester<ItemDtoBookingInfo> jsonDtoBookingInfo;
    @Autowired
    private JacksonTester<ItemDtoRequestInfo> jsonDtoRequestInfo;
    @Autowired
    private JacksonTester<ItemDtoBookingResponse> jsonDtoBookingResponse;

    private final User user = User.builder()
            .id(1L)
            .email("test@test.test")
            .name("Name")
            .build();

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateItemAndSerializableHim() throws Exception {

        final Item item = Item.builder()
                .id(1L)
                .name("TestName")
                .description("test")
                .owner(user)
                .available(true)
                .build();

        JsonContent<Item> result = json.write(item);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("TestName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathValue("$.owner").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.owner").extracting("name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathValue("$.owner").extracting("email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateItemDtoAndSerializableHim() throws Exception {

        final ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("TestName")
                .description("testDescription")
                .owner(1L)
                .available(true)
                .build();

        JsonContent<ItemDto> result = jsonDto.write(itemDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("TestName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("testDescription");
        assertThat(result).extractingJsonPathNumberValue("$.owner").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateItemDtoRequestInfoAndSerializableHim() throws Exception {

        final ItemDtoRequestInfo itemDtoRequestInfo = ItemDtoRequestInfo.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .requestId(1L)
                .build();

        JsonContent<ItemDtoRequestInfo> result = jsonDtoRequestInfo.write(itemDtoRequestInfo);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateItemDtoBookingInfoAndSerializableHim() throws Exception {

        final ItemDtoBookingInfo itemDtoBookingInfo = ItemDtoBookingInfo.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        JsonContent<ItemDtoBookingInfo> result = jsonDtoBookingInfo.write(itemDtoBookingInfo);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateItemDtoBookingResponseAndSerializableHim() throws Exception {

        final ItemDtoBookingResponse itemDtoBookingResponse = ItemDtoBookingResponse.builder()
                .id(1L)
                .name("test")
                .build();

        JsonContent<ItemDtoBookingResponse> result = jsonDtoBookingResponse.write(itemDtoBookingResponse);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
    }

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
