package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.entity.model.BookingDtoInfo;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.CommentDtoInfo;
import ru.practicum.shareit.item.error.CommentIllegalException;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ItemServiceTest {
    private final User user = User.builder()
            .id(1L)
            .email("test@test.test")
            .name("test")
            .build();
    private final CommentDtoInfo commentDtoInfo = new CommentDtoInfo() {
        @Override
        public long getId() {
            return 1L;
        }

        @Override
        public String getText() {
            return "test";
        }

        @Override
        public User getAuthor() {
            return user;
        }

        @Override
        public Timestamp getCreated() {
            return Timestamp.from(Instant.now());
        }
    };
    private final BookingDtoInfo bookingDtoInfo = new BookingDtoInfo() {
        @Override
        public long getId() {
            return 1L;
        }

        @Override
        public User getBooker() {
            return user;
        }

        @Override
        public BookingStatus getStatus() {
            return BookingStatus.APPROVED;
        }
    };
    @Autowired
    ItemService itemService;
    @MockBean
    private UserService mockUserService;
    @MockBean
    private ItemRepository mockItemRepository;
    @MockBean
    private BookingRepository mockBookingRepository;
    @MockBean
    private CommentRepository mockCommentRepository;
    private Item itemTest;
    private Comment commentTest;

    @BeforeEach
    void setUp() {
        itemTest = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .owner(user)
                .available(true)
                .build();
        commentTest = Comment.builder()
                .id(1L)
                .item(itemTest)
                .author(user)
                .text("test")
                .created(Timestamp.from(Instant.now()))
                .build();
        reset(
                mockUserService,
                mockItemRepository,
                mockBookingRepository,
                mockCommentRepository
        );
        when(mockItemRepository
                .findById(1L))
                .thenReturn(Optional.ofNullable(itemTest));
        when(mockUserService.getById(1L))
                .thenReturn(user);
        when(mockBookingRepository
                .getFirst2ByItemIdAndStatusOrderByStartAsc(anyLong(), any(BookingStatus.class)))
                .thenReturn(List.of(bookingDtoInfo));
        when(mockCommentRepository
                .findAllByItemId(1L)).thenReturn(List.of(commentDtoInfo));
    }

    @AfterEach
    void mockVerify() {
        verifyNoMoreInteractions(
                mockUserService,
                mockItemRepository,
                mockBookingRepository,
                mockCommentRepository
        );
    }

    @Test
    @DisplayName("?????????????????? ?????????? add ?????????????? Item.")
    void whenCheckAddMethod() {
        when(mockItemRepository.save(itemTest)).thenReturn(itemTest);

        //test
        Item item = itemService.add(itemTest, 1L);
        assertEquals(1L, item.getId());
        assertEquals("test", item.getName());
        assertEquals("test", item.getDescription());
        assertEquals(user, item.getOwner());
        assertEquals(true, item.getAvailable());

        verify(mockUserService, times(1)).getById(1L);
        verify(mockItemRepository, times(1)).save(itemTest);
    }

    @Test
    @DisplayName("?????????????????? ?????????? getById ?????????????? Item.")
    void whenCheckoutGetByIdMethod() {
        //test
        Item item = itemService.getById(1L, 1L);
        assertEquals(1L, item.getId());
        assertEquals("test", item.getName());
        assertEquals("test", item.getDescription());
        assertEquals(user, item.getOwner());
        assertEquals(true, item.getAvailable());

        verify(mockItemRepository, times(1)).findById(1L);
        verify(mockBookingRepository, times(1)).getFirst2ByItemIdAndStatusOrderByStartAsc(anyLong(), any(BookingStatus.class));
        verify(mockCommentRepository, times(1)).findAllByItemId(1L);
    }

    @Test
    @DisplayName("?????????????????? ?????????? getAll ?????????????? Item.")
    void whenCheckoutGetAllMethod() {
        when(mockItemRepository
                .findAllByOwnerIdOrderByIdAsc(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(itemTest));

        //test
        List<Item> items = itemService.getAll(1L, 0, 10);
        assertEquals(1, items.size());
        assertEquals(itemTest, items.get(0));

        verify(mockItemRepository, times(1)).findAllByOwnerIdOrderByIdAsc(1L, PageRequest.of(0, 10));
        verify(mockBookingRepository, times(1)).getFirst2ByItemIdAndStatusOrderByStartAsc(anyLong(), any(BookingStatus.class));
        verify(mockCommentRepository, times(1)).findAllByItemId(1L);
    }

    @Test
    @DisplayName("?????????????????? ?????????? update ?????????????? Item")
    void whenCheckoutUpdateMethod() {
        when(mockItemRepository.save(itemTest)).thenReturn(itemTest);

        //test
        Item item = itemService.update(itemTest, 1L, 1L);
        assertEquals(1L, item.getId());
        assertEquals("test", item.getName());
        assertEquals("test", item.getDescription());
        assertEquals(user, item.getOwner());
        assertEquals(true, item.getAvailable());

        verify(mockItemRepository, times(1)).findById(1L);
        verify(mockItemRepository, times(1)).save(itemTest);
    }

    @Test
    @DisplayName("?????????????????? ?????????? search ?????????????? Item")
    void whenCheckoutSearchMethod() {
        when(mockItemRepository
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(), any()))
                .thenReturn(List.of(itemTest));

        //test
        List<Item> itemsOk = itemService.search("test", 0, 10);
        assertEquals(1, itemsOk.size());
        assertEquals(itemTest, itemsOk.get(0));

        //test
        List<Item> items = itemService.search("", 0, 10);
        assertEquals(0, items.size());

        verify(mockItemRepository, times(1))
                .findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("?????????????????? ?????????? addComment ?????????????? Item")
    void whenCheckoutAddCommentMethod() {
        when(mockBookingRepository
                .findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(anyLong(), anyLong(), any(BookingStatus.class), any(Timestamp.class)))
                .thenReturn(Optional.of(Booking.builder()
                        .id(1L)
                        .booker(user)
                        .item(itemTest)
                        .start(Timestamp.from(Instant.now()))
                        .end(Timestamp.from(Instant.now()))
                        .status(BookingStatus.WAITING)
                        .build()));
        when(mockCommentRepository.save(commentTest)).thenReturn(commentTest);

        //test
        Comment comment = itemService.addComment(commentTest, 1L, 1L);
        assertEquals(1L, comment.getId());
        assertEquals(itemTest, comment.getItem());
        assertEquals(user, comment.getAuthor());
        assertEquals("test", comment.getText());
        assertEquals(commentTest.getCreated(), comment.getCreated());

        verify(mockItemRepository, times(1)).findById(1L);
        verify(mockUserService, times(1)).getById(1L);
        verify(mockBookingRepository, times(1))
                .findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(anyLong(), anyLong(), any(BookingStatus.class), any(Timestamp.class));
        verify(mockBookingRepository, times(1)).getFirst2ByItemIdAndStatusOrderByStartAsc(anyLong(), any(BookingStatus.class));
        verify(mockCommentRepository, times(1)).save(commentTest);
        verify(mockCommentRepository, times(1)).findAllByItemId(1L);
    }

    @Test
    @DisplayName("???????? ???????????????????????????? Id ?? ?????????? ItemNotFoundException")
    void testFindByIdNotFound() {
        final RuntimeException exceptionId = assertThrows(
                ItemNotFoundException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.getById(2L, 2L);
                    }
                });

        //test
        assertEquals(exceptionId.getMessage(), String.format("???????? ?? id: %s ???? ??????????????.", 2),
                "?????? ???????????????????????????? id ???????????? ?????????????????????? ????????????");

        verify(mockItemRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("???????? ???????????????????????????? Id ?? ?????????? CommentIllegalException")
    void testCommentIllegal() {
        when(mockBookingRepository
                .findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(anyLong(), anyLong(), any(BookingStatus.class), any(Timestamp.class)))
                .thenReturn(Optional.empty());

        final RuntimeException exceptionComment = assertThrows(
                CommentIllegalException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.addComment(commentTest, 1, 2);
                    }
                });

        assertEquals(exceptionComment.getMessage(), String.format("???????????????????????? ?? id: %s ???? ?????????? ???????????????? ?????????????????????? ?????? ???????? id: %s.", 1, 2),
                "???????????????????????? ???? ?????????????????????????? ???????? ???? ?????????? ???? ????????????????????????????");

        verify(mockBookingRepository, times(1))
                .findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(anyLong(), anyLong(), any(BookingStatus.class), any(Timestamp.class));
    }

    @Test
    @DisplayName("???????? ???????????????????????????? Id ?? ?????????? ItemNotFoundByUserException")
    void testItemNotFoundByUserException() {
        final RuntimeException exceptionItemByUser = assertThrows(
                ItemNotFoundByUserException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        itemService.update(itemTest, 1, 2L);
                    }
                });

        //test
        assertEquals(exceptionItemByUser.getMessage(), String.format("???????? ?? id: %s ?? ???????????????????????? ?? id: %s ???? ??????????????.", 1, 2L),
                "?? ???????????????????????? ???? ???????????? ???????? ?????????? ????????.");

        verify(mockItemRepository, times(1)).findById(1L);
    }
}
