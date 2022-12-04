package ru.practicum.shareit.item.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.entity.model.CommentDto;
import ru.practicum.shareit.user.entity.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Timestamp;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class CommentTest {

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
    @Autowired
    private JacksonTester<Comment> json;
    @Autowired
    private JacksonTester<CommentDto> jsonDto;

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateCommentAndSerializableHim() throws Exception {

        final Comment comment = Comment.builder()
                .id(1L)
                .item(item)
                .author(user)
                .text("test")
                .created(timestamp)
                .build();

        JsonContent<Comment> result = json.write(comment);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.author").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.author").extracting("name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathValue("$.author").extracting("email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateCommentDtoAndSerializableHim() throws Exception {

        final CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("test")
                .authorName(user.getName())
                .created(timestamp)
                .build();

        JsonContent<CommentDto> result = jsonDto.write(commentDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotBlank();
    }

    @Test
    @DisplayName("Создаем проверяемый объект без ошибок валидации.")
    void whenCreateValidCommentDtoThenCreated() {

        final CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("test")
                .authorName(user.getName())
                .created(timestamp)
                .build();

        //test
        assertEquals(0, testingValidator(commentDto).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Создаем проверяемый объект с ошибками валидации.")
    void whenCreateValidCommentDtoThenNotCreated() {

        final CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("")
                .authorName(user.getName())
                .created(timestamp)
                .build();

        Set<ConstraintViolation<CommentDto>> errors = testingValidator(commentDto);
        errors.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

        //test
        assertEquals(1, errors.size(), "Размер списка должен быть равен 1.");
    }

    private Set<ConstraintViolation<CommentDto>> testingValidator(CommentDto commentDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(commentDto);
    }
}
