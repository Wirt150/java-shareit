package ru.practicum.shareit.booking.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.entity.model.BookingDtoRequest;
import ru.practicum.shareit.booking.entity.model.BookingDtoResponse;
import ru.practicum.shareit.item.entity.model.ItemDto;
import ru.practicum.shareit.item.web.ItemController;
import ru.practicum.shareit.user.entity.model.UserDto;
import ru.practicum.shareit.user.web.UserController;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTest {
    private static final long BOOKING_ID_ONE = 1L;
    private static final long BOOKING_ID_TWO = 2L;
    private static final long USER_ID_ONE = 1L;
    private static final long USER_ID_TWO = 2L;
    @Autowired
    private ItemController itemController;
    @Autowired
    private UserController userController;
    @Autowired
    private BookingController bookingController;

    private ItemDto itemDtoTestOne = ItemDto.builder()
            .id(0L)
            .name("TestName1")
            .description("testDescription1")
            .owner(1L)
            .available(true)
            .build();

    private ItemDto itemDtoTestTwo = ItemDto.builder()
            .id(0L)
            .name("TestName2")
            .description("testDescription2")
            .owner(1L)
            .available(true)
            .build();

    private UserDto userDtoTestOne = UserDto.builder()
            .id(0L)
            .name("TestName")
            .email("test@test.test")
            .build();

    private UserDto userDtoTestTwo = UserDto.builder()
            .id(0L)
            .name("TestName2")
            .email("test@test.test2")
            .build();

    private final BookingDtoResponse bookingDtoResponse1 = BookingDtoResponse.builder()
            .itemId(1)
            .start(LocalDateTime.of(2024, 1, 1, 1, 1))
            .end(LocalDateTime.of(2025, 1, 1, 1, 1))
            .build();

    private final BookingDtoResponse bookingDtoResponse2 = BookingDtoResponse.builder()
            .itemId(2)
            .start(LocalDateTime.of(2024, 1, 1, 1, 1))
            .end(LocalDateTime.of(2025, 1, 1, 1, 1))
            .build();

    @BeforeEach
    void setUp() {
        userDtoTestOne = userController.create(userDtoTestOne);
        userDtoTestTwo = userController.create(userDtoTestTwo);
        itemDtoTestOne = itemController.create(itemDtoTestOne, USER_ID_ONE);
        itemDtoTestTwo = itemController.create(itemDtoTestTwo, USER_ID_TWO);
    }

    @Test
    @DisplayName("Проверяем метод GET(все id) контроллера booking.")
    void whenCheckGetAllIdMethod() {
        bookingController.create(bookingDtoResponse1, USER_ID_TWO);
        bookingController.create(bookingDtoResponse2, USER_ID_ONE);

        //test
        List<BookingDtoRequest> bookingDtoRequests1 = bookingController.findAll(USER_ID_TWO, "ALL");
        List<BookingDtoRequest> bookingDtoRequests2 = bookingController.findAll(USER_ID_ONE, "ALL");

        assertEquals(1, bookingDtoRequests1.size(), "Размер списка должен быть равен 1.");
        assertEquals(bookingDtoResponse1.getItemId(), bookingDtoRequests1.get(0).getItem().getId(), "Вещи должны совпадать.");
        assertEquals(1, bookingDtoRequests2.size(), "Размер списка должен быть равен 1.");
        assertEquals(bookingDtoResponse1.getItemId(), bookingDtoRequests1.get(0).getItem().getId(), "Вещи должны совпадать.");
    }

    @Test
    @DisplayName("Проверяем метод GET owner (все id) контроллера booking.")
    void whenCheckGetAllIdOwnerMethod() {
        bookingController.create(bookingDtoResponse1, USER_ID_TWO);
        bookingController.create(bookingDtoResponse2, USER_ID_ONE);

        //test
        List<BookingDtoRequest> bookingDtoRequests1 = bookingController.findAllOwner(USER_ID_ONE, "ALL");
        List<BookingDtoRequest> bookingDtoRequests2 = bookingController.findAllOwner(USER_ID_ONE, "ALL");

        assertEquals(1, bookingDtoRequests1.size(), "Размер списка должен быть равен 1.");
        assertEquals(bookingDtoResponse1.getItemId(), bookingDtoRequests1.get(0).getItem().getId(), "Вещи должны совпадать.");
        assertEquals(1, bookingDtoRequests2.size(), "Размер списка должен быть равен 1.");
        assertEquals(bookingDtoResponse1.getItemId(), bookingDtoRequests1.get(0).getItem().getId(), "Вещи должны совпадать.");
    }


    @Test
    @DisplayName("Проверяем метод GET(id) контроллера booking. Проверяем метод POST контроллера booking.")
    void whenCheckGetIdAndCreateMethod() {
        bookingController.create(bookingDtoResponse1, USER_ID_TWO);
        bookingController.create(bookingDtoResponse2, USER_ID_ONE);

        //test
        BookingDtoRequest bookingDtoRequests1 = bookingController.findById(BOOKING_ID_ONE, USER_ID_TWO);
        assertEquals(1, bookingDtoRequests1.getId(), "Id должен совпадать.");
        assertEquals(USER_ID_TWO, bookingDtoRequests1.getBooker().getId(), "Id должен совпадать.");
        assertEquals(itemDtoTestOne.getId(), bookingDtoRequests1.getItem().getId(), "Id должен совпадать.");
        assertEquals(BookingStatus.WAITING, bookingDtoRequests1.getStatus(), "Статус должен совпадать.");

        BookingDtoRequest bookingDtoRequests2 = bookingController.findById(BOOKING_ID_TWO, USER_ID_ONE);
        assertEquals(2, bookingDtoRequests2.getId(), "Id должен совпадать.");
        assertEquals(USER_ID_ONE, bookingDtoRequests2.getBooker().getId(), "Id должен совпадать.");
        assertEquals(itemDtoTestTwo.getId(), bookingDtoRequests2.getItem().getId(), "Id должен совпадать.");
        assertEquals(BookingStatus.WAITING, bookingDtoRequests2.getStatus(), "Статус должен совпадать.");

        assertNotEquals(bookingDtoRequests1, bookingDtoRequests2, "Объекты не должны совпадать");
    }

    @Test
    @DisplayName("Проверяем метод PATCH контроллера booking.")
    void whenCheckUpdateMethod() {
        BookingDtoRequest bookingDtoRequests;
        bookingController.create(bookingDtoResponse1, USER_ID_TWO);
        bookingController.create(bookingDtoResponse2, USER_ID_ONE);

        bookingController.update(BOOKING_ID_ONE, USER_ID_ONE, true);

        //test
        bookingDtoRequests = bookingController.findById(BOOKING_ID_ONE, USER_ID_ONE);
        assertEquals(BookingStatus.APPROVED, bookingDtoRequests.getStatus(), "Статус должен совпадать.");

        bookingController.update(BOOKING_ID_TWO, USER_ID_TWO, false);

        //test
        bookingDtoRequests = bookingController.findById(BOOKING_ID_TWO, USER_ID_TWO);
        assertEquals(BookingStatus.REJECTED, bookingDtoRequests.getStatus(), "Статус должен совпадать.");
    }
}
