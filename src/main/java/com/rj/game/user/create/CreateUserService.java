package com.rj.game.user.create;

import com.rj.game.user.domain.UserId;

public interface CreateUserService {

    UserId createUser(CreateUserRequest createUserRequest);
}
