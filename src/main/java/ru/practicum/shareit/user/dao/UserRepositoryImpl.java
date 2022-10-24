package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users;
    private Long id = 1L;

    @Override
    public Optional<User> addDto(User user) {
        user.setId(id++);
        if (emailCheck(user)) {
            --id;
            return Optional.empty();
        }
        users.put(user.getId(), user);
        return Optional.of(users.get(user.getId()));
    }

    @Override
    public Optional<User> getDtoById(long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllDto() {
        return users.values().stream()
                .sorted(Comparator.comparingLong(User::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> updateDto(User user, long id) {
        if (emailCheck(user)) {
            return Optional.empty();
        }
        User userUpdate = users.get(id);
        Optional.ofNullable(user.getName()).ifPresent(userUpdate::setName);
        Optional.ofNullable(user.getEmail()).ifPresent(userUpdate::setEmail);
        return Optional.of(userUpdate);
    }

    @Override
    public void deleteDto(long id) {
        users.remove(id);
    }

    private boolean emailCheck(User user) {
        for (User userEmailSearch : users.values()) {
            if (userEmailSearch.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
