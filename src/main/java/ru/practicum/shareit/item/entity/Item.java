package ru.practicum.shareit.item.entity;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.booking.entity.model.BookingDtoInfo;
import ru.practicum.shareit.item.entity.model.CommentDtoInfo;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    @ToString.Exclude
    private User owner;
    @Column(name = "name")
    private String name;
    @Column(name = "description", length = 1024)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
