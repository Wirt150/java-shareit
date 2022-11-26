package ru.practicum.shareit.request.web;

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
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.ItemDtoRequestInfo;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.entity.model.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.entity.User;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1));

    @BeforeEach
    void setUp() {
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("test")
                .created(timestamp)
                .author(User.builder()
                        .id(1L)
                        .build())
                .items(List.of(Item.builder()
                        .id(1L)
                        .name("test")
                        .description("test")
                        .available(true)
                        .request(ItemRequest.builder().id(1L).build())
                        .build()))
                .author(User.builder().id(1L).build())
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("test")
                .created(timestamp.toLocalDateTime())
                .items(List.of(
                        ItemDtoRequestInfo.builder()
                                .id(1L)
                                .build()))
                .build();
        reset(
                itemRequestService
        );
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                itemRequestService
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт create сервиса ItemRequest.")
    void createNewItemRequest() throws Exception {
        when(itemRequestService.add(any(ItemRequest.class), anyLong())).thenReturn(itemRequest);

        //test
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.created", is("2020-01-01T01:01:00")))
                .andExpect(jsonPath("$.items", hasSize(1)));

        verify(itemRequestService, times(1)).add(any(ItemRequest.class), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findById сервиса ItemRequest.")
    void findByIdItemRequest() throws Exception {
        when(itemRequestService.getById(anyLong(), anyLong())).thenReturn(itemRequest);

        //test
        mvc.perform(get("/requests/1")
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$.created", is("2020-01-01T01:01:00")))
                .andExpect(jsonPath("$.items", hasSize(1)));

        verify(itemRequestService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAll сервиса ItemRequest.")
    void findAllItemRequest() throws Exception {
        when(itemRequestService.getAll(anyLong())).thenReturn(List.of(itemRequest));

        //test
        mvc.perform(get("/requests")
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", is("2020-01-01T01:01:00")))
                .andExpect(jsonPath("$[0].items", hasSize(1)));

        verify(itemRequestService, times(1)).getAll(anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAllPageable сервиса ItemRequest.")
    void findAllPageableItemRequest() throws Exception {
        when(itemRequestService.getPage(anyInt(), anyInt(), anyLong())).thenReturn(List.of(itemRequest));

        //test
        mvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription())))
                .andExpect(jsonPath("$[0].created", is("2020-01-01T01:01:00")))
                .andExpect(jsonPath("$[0].items", hasSize(1)));

        verify(itemRequestService, times(1)).getPage(anyInt(), anyInt(), anyLong());
    }
}
