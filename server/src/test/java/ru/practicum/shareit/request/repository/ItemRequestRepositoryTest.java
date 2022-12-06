package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@ActiveProfiles("test")
@SpringBootTest(
        properties = {
                "spring.datasource.driverClassName=org.h2.Driver",
                "spring.datasource.url=jdbc:h2:mem:shareit",
                "spring.datasource.username=test",
                "spring.datasource.password=test"
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@Sql(scripts = {
        "classpath:schema.sql",
        "classpath:sql_scripts/sql_scripts_item_request.sql"},
        executionPhase = BEFORE_TEST_METHOD)
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    @DisplayName("Проверяем кастомный метод репозитория findListItem")
    void testCustomRepositoryMethod() {
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByAuthorIdOrderByCreatedAsc(1L);

        //test
        assertThat(itemRequests, hasSize(1));

        ItemRequest itemRequest = itemRequests.get(0);

        //test
        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo("test"));
        assertThat(itemRequest.getAuthor().getId(), equalTo(1L));
    }

    @Test
    @DisplayName("Проверяем кастомный метод репозитория findPageItem")
    void testCustomRepositoryPageMethod() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByAuthorIdNotOrderByCreatedAsc(PageRequest.of(0, 10), 2L).getContent();

        //test
        assertThat(itemRequests, hasSize(1));

        ItemRequest itemRequest = itemRequests.get(0);

        //test
        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo("test"));
        assertThat(itemRequest.getAuthor().getId(), equalTo(1L));
    }
}
