package ru.practicum.shareit.user.dao;


import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {

    Optional<User> addDto(User user);

    Optional<User> getDtoById(long id);

    List<User> getAllDto();

    Optional<User> updateDto(User user, long id);

    void deleteDto(long id);
}
