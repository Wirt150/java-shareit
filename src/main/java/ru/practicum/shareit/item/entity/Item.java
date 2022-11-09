package ru.practicum.shareit.item.entity;

import lombok.*;
import ru.practicum.shareit.booking.entity.model.BookingDtoInfo;
import ru.practicum.shareit.item.entity.model.CommentDtoInfo;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "available")
    private Boolean available;
    @Transient
    @Builder.Default
    private List<CommentDtoInfo> comments = new ArrayList<>();
    @Transient
    @ToString.Exclude
    private BookingDtoInfo lastBooking;
    @Transient
    @ToString.Exclude
    private BookingDtoInfo nextBooking;
}
