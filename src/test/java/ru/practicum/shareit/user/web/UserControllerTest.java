package ru.practicum.shareit.user.web;

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
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.entity.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .build();
        userDto = UserDto.builder()
                .id(1L)
                .email("test@test.test")
                .name("test")
                .build();
        reset(
                userService
        );
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                userService
        );
    }

    @Test
    @DisplayName("Проверяем эндпоинт create сервиса User.")
    void createNewUserRequest() throws Exception {
        when(userService.add(any(User.class))).thenReturn(user);

        //test
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));

        verify(userService, times(1)).add(any(User.class));
    }

    @Test
    @DisplayName("Проверяем эндпоинт findById сервиса User.")
    void findByIdUserRequest() throws Exception {
        when(userService.getById(anyLong())).thenReturn(user);

        //test
        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));

        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Проверяем эндпоинт findAll сервиса User.")
    void findAllUserRequest() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));

        //test
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].name", is(user.getName())));

        verify(userService, times(1)).getAll();
    }

    @Test
    @DisplayName("Проверяем эндпоинт update сервиса User.")
    void updateUserRequest() throws Exception {
        when(userService.update(user, 1L)).thenReturn(user);

        //test
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));

        verify(userService, times(1)).update(user, 1L);
    }

    @Test
    @DisplayName("Проверяем эндпоинт delete сервиса User.")
    void deleteUserRequest() throws Exception {
        //test
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(1L);
    }
}
