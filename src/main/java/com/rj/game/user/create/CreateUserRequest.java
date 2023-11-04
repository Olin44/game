package com.rj.game.user.create;

public record CreateUserRequest(
        String username,
        String password,
        String email,
        String firstname,
        String lastname
) {
}
