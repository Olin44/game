package com.rj.game.user.create;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class UserIdExtractor {

    private static final String UUID_REGEX = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";
    private static final Pattern UUID_REGEX_PATTERN = Pattern.compile(UUID_REGEX);
    public UserId extract(URI uri) {

        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }

        final Matcher matcher = UUID_REGEX_PATTERN.matcher(uri.getPath());
        if(matcher.find()) {
            return new UserId(UUID.fromString(matcher.group(0)));
        } else {
            throw new ExtractUserIdException("UserId not found in location link");
        }
    }
}
