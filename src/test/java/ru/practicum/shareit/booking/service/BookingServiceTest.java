package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.error.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @MockBean
    private BookingRepository mockBookingRepository;
    @MockBean
    private ItemService mockItemService;
    @MockBean
    private UserService mockUserService;
    private Booking bookingTest;
    private Item item;
    private final Timestamp timestampStart = Timestamp.valueOf(LocalDateTime.of(2022, 10, 10, 10, 10));
    private final Timestamp timestampEnd = Timestamp.valueOf(LocalDateTime.of(2023, 10, 10, 10, 10));
    private final User user = User.builder()
            .id(1L)
            .email("test@test.test")
            .name("test")
            .build();

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .owner(user)
                .available(true)
                .build();
        bookingTest = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .item(item)
                .start(timestampStart)
                .end(timestampEnd)
                .status(BookingStatus.WAITING)
                .build();

        reset(
                mockBookingRepository,
                mockUserService,
                mockItemService
        );
        when(mockBookingRepository
                .findById(1L))
                .thenReturn(Optional.ofNullable(bookingTest));
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                mockBookingRepository,
                mockUserService,
                mockItemService
        );
    }

    @Test
    @DisplayName("Проверяем метод add сервиса Booking.")
    void whenCheckAddMethod() {
        when(mockItemService.getById(1L, 1L)).thenReturn(item);
        when(mockUserService.getById(2L)).thenReturn(User.builder().id(2L).build());
        when(mockBookingRepository.save(bookingTest)).thenReturn(bookingTest);

        //test
        Booking booking = bookingService.add(bookingTest, 1L);
        assertEquals(1L, booking.getId());
        assertEquals(2L, booking.getBooker().getId());
        assertEquals(item, booking.getItem());
        assertEquals(timestampStart, booking.getStart());
        assertEquals(timestampEnd, booking.getEnd());
        assertEquals(BookingStatus.WAITING, booking.getStatus());

        verify(mockItemService, times(2)).getById(1L, 1L);
        verify(mockUserService, times(1)).getById(2L);
        verify(mockBookingRepository, times(1)).save(booking);
    }

    @Test
    @DisplayName("Проверяем метод getById сервиса Booking.")
    void whenCheckoutGetByIdMethod() {
        when(mockBookingRepository.findById(1L)).thenReturn(Optional.ofNullable(bookingTest));

        //test
        Booking booking = bookingService.getById(1L, 1L);
        assertEquals(1L, booking.getId());
        assertEquals(2L, booking.getBooker().getId());
        assertEquals(item, booking.getItem());
        assertEquals(timestampStart, booking.getStart());
        assertEquals(timestampEnd, booking.getEnd());
        assertEquals(BookingStatus.WAITING, booking.getStatus());

        verify(mockBookingRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Проверяем метод findAll сервиса Item.")
    void whenCheckoutFindAllMethod() {
        when(mockBookingRepository
                .findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(bookingTest)));
        //test
        List<Booking> all = bookingService.findAll(1L, "ALL", 0, 10);
        assertEquals(1L, all.get(0).getId());
        assertEquals(2L, all.get(0).getBooker().getId());
        assertEquals(item, all.get(0).getItem());
        assertEquals(timestampStart, all.get(0).getStart());
        assertEquals(timestampEnd, all.get(0).getEnd());
        assertEquals(BookingStatus.WAITING, all.get(0).getStatus());

        when(mockBookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> waiting = bookingService.findAll(1L, "WAITING", 0, 10);
        assertEquals(waiting.size(), 1);

        when(mockBookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> rejected = bookingService.findAll(1L, "REJECTED", 0, 10);
        assertEquals(rejected.size(), 1);

        when(mockBookingRepository
                .findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> future = bookingService.findAll(1L, "FUTURE", 0, 10);
        assertEquals(future.size(), 1);


        when(mockBookingRepository
                .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> current = bookingService.findAll(1L, "CURRENT", 0, 10);
        assertEquals(current.size(), 1);

        when(mockBookingRepository
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> past = bookingService.findAll(1L, "PAST", 0, 10);
        assertEquals(past.size(), 1);

        verify(mockUserService, times(6)).getById(1L);
        verify(mockBookingRepository, times(1))
                .findAll(any(PageRequest.class));
        verify(mockBookingRepository, times(2))
                .findAllByBookerIdAndStatusOrderByStartDesc(anyLong(), any(BookingStatus.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод findAllOwner сервиса Item.")
    void whenCheckoutFindAllOwnerMethod() {
        when(mockBookingRepository
                .findAllByItemOwnerIdOrderByStartDesc(anyLong(), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));
        //test
        List<Booking> all = bookingService.findAllBookingByOwner(1L, "ALL", 0, 10);
        assertEquals(1L, all.get(0).getId());
        assertEquals(2L, all.get(0).getBooker().getId());
        assertEquals(item, all.get(0).getItem());
        assertEquals(timestampStart, all.get(0).getStart());
        assertEquals(timestampEnd, all.get(0).getEnd());
        assertEquals(BookingStatus.WAITING, all.get(0).getStatus());

        when(mockBookingRepository
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> waiting = bookingService.findAllBookingByOwner(1L, "WAITING", 0, 10);
        assertEquals(waiting.size(), 1);

        when(mockBookingRepository
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> rejected = bookingService.findAllBookingByOwner(1L, "REJECTED", 0, 10);
        assertEquals(rejected.size(), 1);

        when(mockBookingRepository
                .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> future = bookingService.findAllBookingByOwner(1L, "FUTURE", 0, 10);
        assertEquals(future.size(), 1);


        when(mockBookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> current = bookingService.findAllBookingByOwner(1L, "CURRENT", 0, 10);
        assertEquals(current.size(), 1);

        when(mockBookingRepository
                .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class)))
                .thenReturn(List.of(bookingTest));

        //test
        List<Booking> past = bookingService.findAllBookingByOwner(1L, "PAST", 0, 10);
        assertEquals(past.size(), 1);

        verify(mockUserService, times(6)).getById(1L);
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerIdOrderByStartDesc(anyLong(), any(PageRequest.class));
        verify(mockBookingRepository, times(2))
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(BookingStatus.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(Timestamp.class), any(Timestamp.class), any(PageRequest.class));
        verify(mockBookingRepository, times(1))
                .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(Timestamp.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("Проверяем метод approved сервиса Booking")
    void whenCheckoutApprovedMethod() {
        when(mockBookingRepository.save(bookingTest)).thenReturn(bookingTest);

        //test
        Booking bookings = bookingService.approved(1L, 1L, true);
        assertEquals(1L, bookings.getId());
        assertEquals(2L, bookings.getBooker().getId());
        assertEquals(item, bookings.getItem());
        assertEquals(timestampStart, bookings.getStart());
        assertEquals(timestampEnd, bookings.getEnd());
        assertEquals(BookingStatus.APPROVED, bookings.getStatus());

        verify(mockBookingRepository, times(1)).findById(1L);
        verify(mockBookingRepository, times(1)).save(bookingTest);
    }

    @Test
    @DisplayName("Ловим BookingNotFoundException")
    void testFindByIdNotFound() {
        final EntityNotFoundException bookingNotFoundException = assertThrows(
                BookingNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.getById(100L, 100L);
                    }
                });

        //test
        assertEquals(String.format("Бронирование с id: %s не найдено.", 100L), bookingNotFoundException.getMessage(),
                "При несуществующем id должна выброситься ошибка.");

        verify(mockBookingRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Ловим ItemNotAvailableException")
    void testItemNotAvailableException() {
        item.setAvailable(false);
        when(mockItemService.getById(1L, 1L)).thenReturn(item);

        final RuntimeException itemNotAvailableException = assertThrows(
                ItemNotAvailableException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.add(bookingTest, 1L);
                    }
                });

        //test
        assertEquals(String.format("Вещь с id: %s недоступна для бронирования.", 1L), itemNotAvailableException.getMessage(),
                "При статусе доступа false должна выброситься ошибка.");

        verify(mockItemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Ловим ItemNotAccessException")
    void testItemNotAccessException() {
        item.setOwner(User.builder().id(2L).build());
        when(mockItemService.getById(anyLong(), anyLong())).thenReturn(item);

        final RuntimeException itemNotAccessException = assertThrows(
                ItemNotAccessException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.add(bookingTest, 1L);
                    }
                });

        //test
        assertEquals(String.format("Бронирование с id: %s не доступно для этого пользователя.", 1L), itemNotAccessException.getMessage(),
                "Ошибка доступа к бронированию определенного пользователя.");

        verify(mockItemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Ловим UnknownStateException")
    void testUnknownStateException() {
        final RuntimeException unknownStateException = assertThrows(
                UnknownStateException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.findAll(1L, "UNSUPPORTED_STATUS", 0, 10);
                    }
                });

        //test
        assertEquals("UNSUPPORTED_STATUS", unknownStateException.getMessage(), "Неизвестный статус.");

        verify(mockUserService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("ЛовимItemNotFoundByUserException")
    void testItemNotFoundByUserException() {
        final RuntimeException itemNotFoundByUserException = assertThrows(
                ItemNotFoundByUserException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.approved(1L, 2L, true);
                    }
                });

        //test
        assertEquals("Вещь с id: 1 у пользователя с id: 2 не найдена.", itemNotFoundByUserException.getMessage());

        verify(mockBookingRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Ловим BookingApproveNotAvailable")
    void testBookingApproveNotAvailable() {
        bookingTest.setStatus(BookingStatus.APPROVED);

        final RuntimeException bookingApproveNotAvailable = assertThrows(
                BookingApproveNotAvailable.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        bookingService.approved(1L, 1L, true);
                    }
                });

        //test
        assertEquals("Статус бронирования : 1 уже подтвержден.", bookingApproveNotAvailable.getMessage());

        verify(mockBookingRepository, times(1)).findById(1L);
    }
}
