package ru.practicum.shareit.item.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {
    private long id;
    private long owner;
    @NotBlank(message = "Наименование не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание не может быть пустым.")
    private String description;
    @NotNull(message = "Статус не может быть пустой.")
    private Boolean available;
    private ItemDtoBookingInfo lastBooking;
    private ItemDtoBookingInfo nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
