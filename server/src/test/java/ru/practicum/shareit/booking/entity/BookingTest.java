package ru.practicum.shareit.booking.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.entity.model.BookingDtoRequest;
import ru.practicum.shareit.booking.entity.model.BookingDtoResponse;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.ItemDtoBookingResponse;
import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookingTest {

    private final Timestamp timestampStart = Timestamp.valueOf("2022-10-10 10:10:00");
    private final Timestamp timestampEnd = Timestamp.valueOf("2023-10-10 10:10:00");
    private final User user = User.builder()
            .id(1L)
            .email("email@email.com")
            .name("test")
            .build();
    @Autowired
    private JacksonTester<Booking> json;
    @Autowired
    private JacksonTester<BookingDtoResponse> jsonDtoResponse;
    @Autowired
    private JacksonTester<BookingDtoRequest> jsonDtoRequest;
    private Item item = Item.builder()
            .id(1L)
            .name("test")
            .description("test")
            .available(true)
            .build();

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateBookingAndSerializableHim() throws Exception {
        final Booking booking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .start(timestampStart)
                .end(timestampEnd)
                .status(BookingStatus.WAITING)
                .build();

        JsonContent<Booking> result = json.write(booking);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.booker").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.booker").extracting("name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathValue("$.booker").extracting("email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathValue("$.item").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.item").extracting("name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathValue("$.item").extracting("description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathValue("$.item").extracting("available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathStringValue("$.start").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.end").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(BookingStatus.WAITING.toString());
        assertThat(booking).isEqualTo(booking);
        assertThat(booking.hashCode()).isEqualTo(booking.hashCode());
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateBookingDtoResponseAndSerializableHim() throws Exception {
        final BookingDtoResponse bookingDtoResponse = BookingDtoResponse.builder()
                .itemId(item.getId())
                .start(timestampStart.toLocalDateTime())
                .end(timestampEnd.toLocalDateTime())
                .build();

        JsonContent<BookingDtoResponse> result = jsonDtoResponse.write(bookingDtoResponse);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-10-10T10:10:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-10-10T10:10:00");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации.")
    void whenCreateBookingDtoRequestAndSerializableHim() throws Exception {
        final BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
                .id(1L)
                .booker(user)
                .item(ItemDtoBookingResponse.builder().build())
                .start(timestampStart.toLocalDateTime())
                .end(timestampEnd.toLocalDateTime())
                .status(BookingStatus.WAITING)
                .build();

        JsonContent<BookingDtoRequest> result = jsonDtoRequest.write(bookingDtoRequest);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.booker").extracting("id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.booker").extracting("name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathValue("$.booker").extracting("email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-10-10T10:10:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-10-10T10:10:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(BookingStatus.WAITING.toString());
    }
}
