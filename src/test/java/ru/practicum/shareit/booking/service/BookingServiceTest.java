package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingState;
import ru.practicum.shareit.booking.error.BookingNotFoundException;
import ru.practicum.shareit.booking.error.ItemNotAccessException;
import ru.practicum.shareit.booking.error.ItemNotAvailableException;
import ru.practicum.shareit.booking.error.UnknownStateException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceTest {
    private static final long USER_ID = 1L;
    private static final long BOOKING_ID_ONE = 1L;
    private static final long BOOKING_ID_TWO = 2L;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    private Item itemTest = Item.builder()
            .id(1L)
            .name("TestName1")
            .description("testDescription1")
            .owner(User.builder().id(1L).build())
            .available(true)
            .build();

    private final User userTest = User.builder()
            .id(1L)
            .email("email@email.com")
            .name("Name")
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .item(itemTest)
            .booker(userTest)
            .build();

    @Test
    @DisplayName("Проверяем ошибки сервисного слоя бронирований.")
    void whenCheckBookingServiceForException() {
        userService.add(userTest);
        itemService.add(itemTest, USER_ID);

        final RuntimeException unknownStateException = assertThrows(
                UnknownStateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.findAll(USER_ID, BookingState.UNSUPPORTED_STATUS.name());
                    }
                });

        //test
        assertEquals("UNSUPPORTED_STATUS", unknownStateException.getMessage(), "Неизвестный статус.");

        final EntityNotFoundException bookingNotFoundException = assertThrows(
                BookingNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.getById(BOOKING_ID_TWO, USER_ID);
                    }
                });

        //test
        assertEquals(String.format("Бронирование с id: %s не найдено.", BOOKING_ID_TWO), bookingNotFoundException.getMessage(),
                "При несуществующем id должна выброситься ошибка.");

        final RuntimeException itemNotAccessException = assertThrows(
                ItemNotAccessException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.add(booking, USER_ID);
                    }
                });

        //test
        assertEquals(String.format("Бронирование с id: %s не доступно для этого пользователя.", BOOKING_ID_ONE), itemNotAccessException.getMessage(),
                "Ошибка доступа к бронированию определенного пользователя.");


        itemTest = Item.builder()
                .id(1L)
                .name("TestName1")
                .description("testDescription1")
                .owner(User.builder().id(1L).build())
                .available(false)
                .build();

        itemService.update(itemTest, BOOKING_ID_ONE, USER_ID);
        final RuntimeException itemNotAvailableException = assertThrows(
                ItemNotAvailableException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.add(booking, USER_ID);
                    }
                });

        //test
        assertEquals(String.format("Вещь с id: %s недоступна для бронирования.", BOOKING_ID_ONE), itemNotAvailableException.getMessage(),
                "При статусе доступа false должна выброситься ошибка.");

    }
}
