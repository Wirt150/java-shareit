package ru.practicum.shareit.request.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.entity.model.ItemRequestDto;
import ru.practicum.shareit.user.entity.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class ItemRequestMapperTest {

    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2022, 10, 10, 10, 10));

    private final ItemRequest itemRequestTest = ItemRequest.builder()
            .id(1L)
            .author(User.builder().id(1L).build())
            .description("test")
            .created(timestamp)
            .items(List.of(Item.builder().id(1L).request(ItemRequest.builder().id(1L).build()).build()))
            .build();

    @Test
    @DisplayName("Проверяем методы маппера ItemRequest.")
    void whenCheckItemRequestMapper() {
        final ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequestTest);

        //test
        assertThat(itemRequestDto, notNullValue());
        assertThat(itemRequestDto.getId(), equalTo(itemRequestTest.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequestTest.getDescription()));
        assertThat(itemRequestDto.getCreated(), equalTo(itemRequestTest.getCreated().toLocalDateTime()));
        assertThat(itemRequestDto.getItems().size(), equalTo(itemRequestTest.getItems().size()));

        final ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, 1);

        //test
        assertThat(itemRequest, notNullValue());
        assertThat(itemRequest.getId(), equalTo(itemRequestTest.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestTest.getDescription()));
        assertThat(itemRequest.getCreated(), notNullValue());
        assertThat(itemRequest.getItems().size(), equalTo(0));
    }
}
