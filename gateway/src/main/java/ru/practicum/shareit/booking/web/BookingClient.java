package ru.practicum.shareit.booking.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.config.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX)).requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> create(final BookingDtoResponse dto, final long userId) {
        return post("", userId, dto);
    }

    //    @Cacheable(cacheNames = "booking")
    public ResponseEntity<Object> findById(final long id, final long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> findAll(int from, int size, long userId, String state) {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> findAllOwner(int from, int size, long userId, String state) {
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    //    @CacheEvict(cacheNames = {"item", "booking"}, allEntries = true)
    public ResponseEntity<Object> update(final long id, final long owner, final boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + id + "?approved={approved}", owner, parameters, null);
    }
}
