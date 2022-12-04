package ru.practicum.shareit.booking.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.entity.model.BookingDtoRequest;
import ru.practicum.shareit.booking.entity.model.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingDtoRequest create(
            @Valid @RequestBody final BookingDtoResponse dto,
            @RequestHeader(USER_ID_HEADER) final long bookerId
    ) {
        return BookingMapper.toBookingDto(bookingService.add(BookingMapper.toBooking(dto, bookerId), bookerId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public BookingDtoRequest findById(
            @PathVariable final long id,
            @RequestHeader(USER_ID_HEADER) final long userId
    ) {
        return BookingMapper.toBookingDto(bookingService.getById(id, userId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookingDtoRequest> findAll(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @RequestHeader(USER_ID_HEADER) final long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL") final String state
    ) {
        return bookingService.findAll(bookerId, state, from, size).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/owner")
    public List<BookingDtoRequest> findAllOwner(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @RequestHeader(USER_ID_HEADER) final long bookerId,
            @RequestParam(value = "state", defaultValue = "ALL") final String state
    ) {
        return bookingService.findAllBookingByOwner(bookerId, state, from, size).stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("{id}")
    public BookingDtoRequest update(
            @PathVariable final long id,
            @RequestHeader(USER_ID_HEADER) final long owner,
            @RequestParam("approved") final boolean approved
    ) {
        return BookingMapper.toBookingDto(bookingService.approved(id, owner, approved));
    }
}
