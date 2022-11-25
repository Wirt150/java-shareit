package ru.practicum.shareit.booking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.entity.model.BookingDtoRequest;
import ru.practicum.shareit.booking.entity.model.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.ItemDtoBookingResponse;
import ru.practicum.shareit.user.entity.User;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private Booking booking;
    private BookingDtoResponse bookingDtoResponse;
    private BookingDtoRequest bookingDtoRequest;
    private final Timestamp timestampStart = Timestamp.valueOf(LocalDateTime.of(2023, 10, 10, 10, 10));
    private final Timestamp timestampEnd = Timestamp.valueOf(LocalDateTime.of(2024, 10, 10, 10, 10));
    private final Item item = Item.builder()
            .id(1L)
            .name("test")
            .description("test")
            .available(true)
            .build();
    private final User user = User.builder()
            .id(1L)
            .email("email@email.com")
            .name("test")
            .build();

    @BeforeEach
    void setUp() {
        booking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .start(timestampStart)
                .end(timestampEnd)
                .status(BookingStatus.WAITING)
                .build();
        bookingDtoRequest = BookingDtoRequest.builder()
                .id(1L)
                .booker(user)
                .item(ItemDtoBookingResponse.builder().build())
                .start(timestampStart.toLocalDateTime())
                .end(timestampEnd.toLocalDateTime())
                .status(BookingStatus.WAITING)
                .build();
        bookingDtoResponse = BookingDtoResponse.builder()
                .itemId(item.getId())
                .start(timestampStart.toLocalDateTime())
                .end(timestampEnd.toLocalDateTime())
                .build();
        reset(
                bookingService
        );
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                bookingService
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт create сервиса Booking.")
    void createNewBooking() throws Exception {
        when(bookingService.add(any(Booking.class), anyLong())).thenReturn(booking);

        //test
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoResponse))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.start", is("2023-10-10T10:10:00")))
                .andExpect(jsonPath("$.end", is("2024-10-10T10:10:00")))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.toString())));

        verify(bookingService, times(1)).add(any(Booking.class), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findById сервиса Booking.")
    void findByIdBooking() throws Exception {
        when(bookingService.getById(anyLong(), anyLong())).thenReturn(booking);

        //test
        mvc.perform(get("/bookings/1")
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.start", is("2023-10-10T10:10:00")))
                .andExpect(jsonPath("$.end", is("2024-10-10T10:10:00")))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.toString())));

        verify(bookingService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAll сервиса Booking.")
    void findAllBooking() throws Exception {
        when(bookingService.findAll(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(booking));

        //test
        mvc.perform(get("/bookings")
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", notNullValue()))
                .andExpect(jsonPath("$[0].item", notNullValue()))
                .andExpect(jsonPath("$[0].start", is("2023-10-10T10:10:00")))
                .andExpect(jsonPath("$[0].end", is("2024-10-10T10:10:00")))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.toString())));

        verify(bookingService, times(1)).findAll(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAllOwner сервиса Booking.")
    void findAllOwnerBooking() throws Exception {
        when(bookingService.findAllOwner(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(booking));

        //test
        mvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "10")
                        .param("state", "ALL")
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker", notNullValue()))
                .andExpect(jsonPath("$[0].item", notNullValue()))
                .andExpect(jsonPath("$[0].start", is("2023-10-10T10:10:00")))
                .andExpect(jsonPath("$[0].end", is("2024-10-10T10:10:00")))
                .andExpect(jsonPath("$[0].status", is(BookingStatus.WAITING.toString())));

        verify(bookingService, times(1)).findAllOwner(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Проверяем эндпоинт update сервиса Booking.")
    void updateBooking() throws Exception {
        when(bookingService.approved(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        //test
        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()))
                .andExpect(jsonPath("$.start", is("2023-10-10T10:10:00")))
                .andExpect(jsonPath("$.end", is("2024-10-10T10:10:00")))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.toString())));

        verify(bookingService, times(1)).approved(anyLong(), anyLong(), anyBoolean());
    }
}
