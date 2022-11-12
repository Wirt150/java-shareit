package ru.practicum.shareit.booking.web;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.entity.model.BookingDtoRequest;
import ru.practicum.shareit.booking.entity.model.BookingDtoResponse;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.ItemDtoBookingResponse;
import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;

@UtilityClass
public class BookingMapper {

    public static BookingDtoRequest toBookingDto(Booking booking) {
        return BookingDtoRequest.builder()
                .id(booking.getId())
                .booker(booking.getBooker())
                .item(ItemDtoBookingResponse.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .build())
                .start(booking.getStart().toLocalDateTime())
                .end(booking.getEnd().toLocalDateTime())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDtoResponse bookingDto, final long userId) {
        return Booking.builder()
                .booker(User.builder()
                        .id(userId)
                        .build())
                .item(Item.builder()
                        .id(bookingDto.getItemId())
                        .build())
                .start(Timestamp.valueOf(bookingDto.getStart()))
                .end(Timestamp.valueOf(bookingDto.getEnd()))
                .status(BookingStatus.WAITING)
                .build();
    }
}
