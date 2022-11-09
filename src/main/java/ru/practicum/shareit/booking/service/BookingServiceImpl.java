package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingState;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.error.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.error.ItemNotFoundByUserException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public List<Booking> findAll(final long bookerId, final String state) {
        Timestamp timestampNow = Timestamp.from(Instant.now());
        switch (bookingStateAndIdCheck(bookerId, state)) {
            case ALL:
                return bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            case FUTURE:
                return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, timestampNow);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, timestampNow, timestampNow);
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, timestampNow);
            default:
                throw new UnknownStateException(state);
        }
    }

    @Override
    public List<Booking> findAllOwner(long ownerId, String state) {
        Timestamp timestampNow = Timestamp.from(Instant.now());
        switch (bookingStateAndIdCheck(ownerId, state)) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId);
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, timestampNow);
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, timestampNow, timestampNow);
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, timestampNow);
            default:
                throw new UnknownStateException(state);
        }
    }

    @Override
    public Booking getById(final long id, final long userId) {
        return bookingRepository.findById(id)
                .filter(b -> b.getBooker().getId() == userId || b.getItem().getOwner().getId() == userId)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Override
    public Booking add(final Booking booking, final long bookerId) {
        Optional<Item> item = Optional.of(itemService.getById(booking.getItem().getId(), bookerId));
        item.filter(Item::getAvailable)
                .orElseThrow(() -> new ItemNotAvailableException(booking.getItem().getId()));

        item.filter(i -> !Objects.equals(i.getOwner().getId(), booking.getBooker().getId()))
                .orElseThrow(() -> new ItemNotAccessException(booking.getItem().getId()));

        booking.setItem(itemService.getById(booking.getItem().getId(), bookerId));
        booking.setBooker(userService.getById(booking.getBooker().getId()));
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approved(final long id, final long owner, final boolean approved) {
        Booking booking = bookingRepository.findById(id)
                .filter(b -> b.getItem().getOwner().getId() == owner)
                .orElseThrow(() -> new ItemNotFoundByUserException(id, owner));
        if (booking.getStatus() != BookingStatus.APPROVED) {
            booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        } else {
            throw new BookingApproveNotAvailable(id);
        }
        return bookingRepository.save(booking);
    }

    private BookingState bookingStateAndIdCheck(long userId, String state) {
        BookingState bookingState = BookingState.UNSUPPORTED_STATUS;
        userService.getById(userId);
        if (Arrays.stream(BookingState.values()).anyMatch(bs -> bs.name().equals(state))) {
            bookingState = BookingState.valueOf(state);
        }
        return bookingState;
    }
}