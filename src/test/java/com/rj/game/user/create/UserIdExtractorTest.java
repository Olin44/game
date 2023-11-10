package com.rj.game.user.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class UserIdExtractorTest {

    private UserIdExtractor userIdExtractor;

    @BeforeEach
    public void setUp() {
        userIdExtractor = new UserIdExtractor();
    }

    @Test
    public void testExtractValidUserId() {
        URI uri = URI.create("/users/6bace418-6488-4f92-9a5c-78d76534aa1a");
        Matcher matcher = Pattern.compile(UserIdExtractor.UUID_REGEX).matcher(uri.getPath());
        assertTrue(matcher.find());
        UUID expectedUUID = UUID.fromString(matcher.group(0));
        assertEquals(new UserId(expectedUUID), userIdExtractor.extract(uri));
    }

    @Test
    public void testExtractInvalidUserId() {
        URI uri = URI.create("/users/invalid-uuid");
        Matcher matcher = Pattern.compile(UserIdExtractor.UUID_REGEX).matcher(uri.getPath());
        assertFalse(matcher.find());
        assertThrows(ExtractUserIdException.class, () -> userIdExtractor.extract(uri));
    }
}

