package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.constart.BookingStatus;
import ru.practicum.shareit.booking.entity.model.BookingDtoInfo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, Timestamp start);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, Timestamp start, Timestamp end);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, Timestamp start);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, Timestamp start, Timestamp end);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, Timestamp end);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, Timestamp end);

    List<BookingDtoInfo> getFirst2ByItemIdAndStatusOrderByStartAsc(long itemId, BookingStatus status);

    Optional<Booking> findFirstByBookerIdAndItemIdAndStatusNotLikeAndEndBefore(Long bookerId, long itemId, BookingStatus status, Timestamp end);
}
