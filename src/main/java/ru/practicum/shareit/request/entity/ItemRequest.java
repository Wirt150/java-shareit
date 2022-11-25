package ru.practicum.shareit.request.entity;

import lombok.*;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ToString.Exclude
    private User author;
    @Column(name = "description", length = 1024)
    private String description;
    @Column(name = "created")
    private Timestamp created;
    @ToString.Exclude
    @Transient
    private List<Item> items;
}
