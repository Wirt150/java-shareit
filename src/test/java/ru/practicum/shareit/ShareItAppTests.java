package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItAppTests {
    @Test
    void main() {
        ShareItApp.main(new String[]{"start"});
    }
}
