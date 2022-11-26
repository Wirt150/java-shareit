package ru.practicum.shareit.item.web;

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
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.CommentDto;
import ru.practicum.shareit.item.entity.model.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private Item item;
    private ItemDto itemDto;

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
        itemDto = ItemDto.builder()
                .id(0L)
                .name("test")
                .description("test")
                .owner(1L)
                .available(true)
                .build();
        reset(
                itemService
        );
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                itemService
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт create сервиса Item.")
    void createNewItem() throws Exception {
        when(itemService.add(any(Item.class), anyLong())).thenReturn(item);

        //test
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.owner", is(user.getId().intValue())))
                .andExpect(jsonPath("$.available", is(true)));

        verify(itemService, times(1)).add(any(Item.class), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findById сервиса Item.")
    void findByIdItem() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(item);

        //test
        mvc.perform(get("/items/1")
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.owner", is(user.getId().intValue())))
                .andExpect(jsonPath("$.available", is(true)));

        verify(itemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAll сервиса Item.")
    void findAllItem() throws Exception {
        when(itemService.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(item));

        //test
        mvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].owner", is(user.getId().intValue())))
                .andExpect(jsonPath("$[0].available", is(true)));

        verify(itemService, times(1)).getAll(anyLong(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Проверяем эндпоинт update сервиса Item.")
    void updateItem() throws Exception {
        when(itemService.update(any(Item.class), anyLong(), anyLong())).thenReturn(item);

        //test
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.owner", is(user.getId().intValue())))
                .andExpect(jsonPath("$.available", is(true)));

        verify(itemService, times(1)).update(any(Item.class), anyLong(), anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт search сервиса Item.")
    void searchItem() throws Exception {
        when(itemService.search(anyString(), anyInt(), anyInt())).thenReturn(List.of(item));

        //test
        mvc.perform(get("/items/search")
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", "test")
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].owner", is(user.getId().intValue())))
                .andExpect(jsonPath("$[0].available", is(true)));

        verify(itemService, times(1)).search(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Проверяем эндпоинт addComment сервиса Item.")
    void addCommentItem() throws Exception {
        final Comment comment = Comment.builder()
                .id(1L)
                .item(item)
                .author(user)
                .text("test")
                .created(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1)))
                .build();

        final CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .authorName(user.getName())
                .text("test")
                .created(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 1, 1)))
                .build();

        when(itemService.addComment(any(Comment.class), anyLong(), anyLong())).thenReturn(comment);

        //test
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header(USER_REQUEST_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthor().getName())))
                .andExpect(jsonPath("$.created", notNullValue()));

        verify(itemService, times(1)).addComment(any(Comment.class), anyLong(), anyLong());
    }
}
