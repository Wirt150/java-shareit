package ru.practicum.shareit.booking.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.entity.model.BookingDtoRequest;
import ru.practicum.shareit.booking.entity.model.BookingDtoResponse;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class BookingMapperTest {

    private final Timestamp timestampStart = Timestamp.valueOf(LocalDateTime.of(2022, 10, 10, 10, 10));
    private final Timestamp timestampEnd = Timestamp.valueOf(LocalDateTime.of(2023, 10, 10, 10, 10));
    private final Booking bookingTest = Booking.builder()
            .id(1L)
            .booker(User.builder().id(1L).build())
            .item(Item.builder().id(1L).build())
            .start(timestampStart)
            .end(timestampEnd)
            .status(BookingStatus.WAITING)
            .build();
    private final BookingDtoResponse bookingDtoResponse = BookingDtoResponse.builder()
            .itemId(1L)
            .start(timestampStart.toLocalDateTime())
            .end(timestampEnd.toLocalDateTime())
            .build();


    @Test
    @DisplayName("Проверяем методы маппера Booking.")
    void whenCheckItemMapper() {
        final BookingDtoRequest bookingDtoRequest = BookingMapper.toBookingDto(bookingTest);

        //test
        assertThat(bookingDtoRequest, notNullValue());
        assertThat(bookingDtoRequest.getId(), equalTo(1L));
        assertThat(bookingDtoRequest.getBooker().getId(), equalTo(1L));
        assertThat(bookingDtoRequest.getItem().getId(), equalTo(1L));
        assertThat(bookingDtoRequest.getStart().toString(), equalTo("2022-10-10T10:10"));
        assertThat(bookingDtoRequest.getEnd().toString(), equalTo("2023-10-10T10:10"));
        assertThat(bookingDtoRequest.getStatus(), equalTo(BookingStatus.WAITING));

        final Booking booking = BookingMapper.toBooking(bookingDtoResponse, 1L);

        //test
        assertThat(booking, notNullValue());
        assertThat(booking.getBooker().getId(), equalTo(1L));
        assertThat(booking.getItem().getId(), equalTo(1L));
        assertThat(booking.getStart().toString(), equalTo("2022-10-10 10:10:00.0"));
        assertThat(booking.getEnd().toString(), equalTo("2023-10-10 10:10:00.0"));
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
    }
}
