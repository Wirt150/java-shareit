package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private long id;
    private long userId;
    @NotBlank(message = "Наименование не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание не может быть пустым.")
    private String description;
    @NotNull(message = "Статус не может быть пустой.")
    private Boolean available;
    private ItemRequest request;
}
