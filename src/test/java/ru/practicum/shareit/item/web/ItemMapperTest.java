package ru.practicum.shareit.item.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.model.ItemDto;
import ru.practicum.shareit.user.entity.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ItemMapperTest {

    private final Item itemTest = Item.builder()
            .id(1L)
            .name("TestName")
            .description("test")
            .owner(User.builder().id(1L).build())
            .available(true)
            .build();


    @Test
    @DisplayName("Проверяем методы маппера Item.")
    void whenCheckItemMapper() {
        final ItemDto itemDto = ItemMapper.toItemDto(itemTest);

        //test
        assertThat(itemDto, notNullValue());
        assertThat(itemDto.getId(), equalTo(itemTest.getId()));
        assertThat(itemDto.getDescription(), equalTo(itemTest.getDescription()));
        assertThat(itemDto.getOwner(), equalTo(itemTest.getOwner().getId()));
        assertThat(itemDto.getAvailable(), equalTo(itemTest.getAvailable()));

        final Item item = ItemMapper.toItem(itemDto, 1L);

        //test
        assertThat(item, notNullValue());
        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getOwner().getId(), equalTo(itemDto.getOwner()));
        assertThat(item.getAvailable(), equalTo(itemDto.getAvailable()));
    }
}
