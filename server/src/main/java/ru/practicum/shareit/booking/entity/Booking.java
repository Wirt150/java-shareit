package ru.practicum.shareit.booking.entity;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker", referencedColumnName = "id")
    private User booker;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item", referencedColumnName = "id")
    private Item item;
    @Column(name = "start")
    private Timestamp start;
    @Column(name = "stop")
    private Timestamp end;
    @Column(name = "confirm_status", length = 10)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

