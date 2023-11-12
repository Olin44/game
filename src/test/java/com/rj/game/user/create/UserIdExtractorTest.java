package com.rj.game.user.create;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class UserIdExtractorTest {

    private UserIdExtractor userIdExtractor;

    @BeforeEach
    public void setUp() {
        userIdExtractor = new UserIdExtractor();
    }


    @ParameterizedTest
    @ValueSource(strings = {"6bace418-6488-4f92-9a5c-78d76534aa1a",
                            "4c83c89c-2e7e-4136-a3cf-57dcba050093",
                            "ee4c3c8e-1642-4019-9725-4f10922b4115"})
    public void testExtractValidUserId(String id) {
        URI uri = URI.create(id);
        UserId expectedUserId = new UserId(UUID.fromString(id));
        assertEquals(expectedUserId, userIdExtractor.extract(uri));
    }

    @Test
    public void testExtractEmptyStringUserId() {
        URI uri = URI.create("");
        ExtractUserIdException exception = Assertions.assertThrows(ExtractUserIdException.class, () -> {
            userIdExtractor.extract(uri);
            assertThrows(ExtractUserIdException.class, () -> userIdExtractor.extract(uri));
        });

        Assertions.assertEquals("UserId not found in location link", exception.getMessage());
    }

    @Test
    public void testExtractNullUserId() {
        URI uri = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userIdExtractor.extract(uri);
        });
    }

    @Test
    public void testExtractInvalidUserId() {
        URI uri = URI.create("/users/invalid-uuid");
        ExtractUserIdException exception = Assertions.assertThrows(ExtractUserIdException.class, () -> {
            userIdExtractor.extract(uri);
        });

        Assertions.assertEquals("UserId not found in location link", exception.getMessage());
    }




}