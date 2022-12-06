package ru.practicum.shareit.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
