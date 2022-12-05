package ru.practicum.shareit.item.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.config.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(final ItemDto dto, final long userId) {
        return post("", userId, dto);
    }

    //    @Cacheable(cacheNames = "item")
    public ResponseEntity<Object> findById(final long id, final long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> findAll(int from, int size, long userId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    //    @CachePut(cacheNames = "item")
    public ResponseEntity<Object> update(final long id, ItemDto dto, final long userId) {
        return patch("/" + id, userId, dto);
    }

    //    @CachePut(cacheNames = "item")
    public ResponseEntity<Object> search(final String text, final int from, final int size, final long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    //    @CacheEvict(cacheNames = "item", allEntries = true)
    public ResponseEntity<Object> addComment(CommentDto dto, long author, long itemId) {
        return post("/" + itemId + "/comment", author, dto);
    }
}
