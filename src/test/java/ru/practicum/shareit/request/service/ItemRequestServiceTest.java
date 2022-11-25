package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.error.ItemRequestNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ItemRequestServiceTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @MockBean
    private ItemRequestRepository mockItemRequestRepository;
    @MockBean
    private ItemRepository mockItemRepository;
    @MockBean
    private UserService mockUserService;
    private ItemRequest itemRequest;
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
                .build();
        when(mockUserService.getById(1L)).thenReturn(User.builder().id(1L).build());
        when(mockItemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
        reset(
                mockItemRequestRepository,
                mockItemRepository,
                mockUserService
        );
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                mockItemRequestRepository,
                mockItemRepository,
                mockUserService
        );
    }

    @Test
    @DisplayName("Проверяем метод add сервиса ItemRequest.")
    void whenCheckAddMethod() {
        when(mockItemRequestRepository.save(itemRequest)).thenReturn(itemRequest);

        //test
        ItemRequest testItemRequest = itemRequestService.add(itemRequest, 1L);
        assertEquals(1L, testItemRequest.getId());
        assertEquals("test", testItemRequest.getDescription());
        assertEquals(timestamp, testItemRequest.getCreated());

        verify(mockUserService, times(1)).getById(1L);
        verify(mockItemRequestRepository, times(1)).save(testItemRequest);
    }

    @Test
    @DisplayName("Проверяем метод getById сервиса ItemRequest.")
    void whenCheckoutGetByIdMethod() {
        when(mockItemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));

        //test
        ItemRequest testItemRequest = itemRequestService.getById(1L, 1L);
        assertEquals(1L, testItemRequest.getId());
        assertEquals("test", testItemRequest.getDescription());
        assertEquals(timestamp, testItemRequest.getCreated());

        verify(mockUserService, times(1)).getById(1L);
        verify(mockItemRepository, times(1)).findAllByRequestId(1L);
        verify(mockItemRequestRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Проверяем метод getAll сервиса ItemRequest.")
    void whenCheckoutGetAllMethod() {
        when(mockItemRequestRepository.findAllByAuthorIdOrderByCreatedAsc(1L)).thenReturn(List.of(itemRequest));

        //test
        List<ItemRequest> testItemRequest = itemRequestService.getAll(1L);
        assertEquals(1, testItemRequest.size());
        assertEquals(itemRequest, testItemRequest.get(0));

        verify(mockUserService, times(1)).getById(1L);
        verify(mockItemRepository, times(1)).findAllByRequestId(1L);
        verify(mockItemRequestRepository, times(1)).findAllByAuthorIdOrderByCreatedAsc(1L);
    }

    @Test
    @DisplayName("Проверяем метод getPage сервиса ItemRequest.")
    void whenCheckoutGetPageMethod() {
        when(mockItemRequestRepository.findAllByAuthorIdNotOrderByCreatedAsc(
                PageRequest.of(0, 10), 1L)).thenReturn(new PageImpl<>(List.of(itemRequest)));

        //test
        List<ItemRequest> testItemRequest = itemRequestService.getPage(0, 10, 1L);
        assertEquals(1, testItemRequest.size());
        assertEquals(itemRequest, testItemRequest.get(0));

        verify(mockUserService, times(1)).getById(1L);
        verify(mockItemRepository, times(1)).findAllByRequestId(1L);
        verify(mockItemRequestRepository, times(1))
                .findAllByAuthorIdNotOrderByCreatedAsc(PageRequest.of(0, 10), 1L);
    }

    @Test
    @DisplayName("Ищем несуществующий Id и ловим ItemRequestNotFoundException")
    void testFindByIdNotFound() {
        //test
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.getById(2L, 1L));

        verify(mockUserService, times(1)).getById(1L);
        verify(mockItemRequestRepository, times(1)).findById(2L);
    }
}
