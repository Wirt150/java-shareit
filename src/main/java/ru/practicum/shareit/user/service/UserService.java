package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.entity.User;

import java.util.List;

public interface UserService {
    User add(User user);

    User getById(long id);

    List<User> getAll();

    User update(User user, long id);

    void delete(long id);

}
