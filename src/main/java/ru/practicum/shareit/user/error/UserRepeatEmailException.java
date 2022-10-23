package ru.practicum.shareit.user.error;

public class UserRepeatEmailException extends RuntimeException{

    public UserRepeatEmailException(String email) {
        super(String.format("Пользователь c таким email: %s существует.", email));
    }
}
