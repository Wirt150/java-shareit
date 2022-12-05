package ru.practicum.shareit.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.config.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(final UserDto dto) {
        return post("", dto);
    }

    //    @Cacheable(cacheNames = "user")
    public ResponseEntity<Object> findById(final long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    //    @CachePut(cacheNames = "user")
    public ResponseEntity<Object> update(final long id, UserDto dto) {
        return patch("/" + id, dto);
    }

    //    @CacheEvict(cacheNames = "user")
    public void delete(final long id) {
        delete("/" + id);
    }
}
