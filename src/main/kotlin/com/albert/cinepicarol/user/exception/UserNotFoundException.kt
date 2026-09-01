package com.albert.cinepicarol.user.exception

import com.albert.cinepicarol.common.exception.DomainException
import java.util.UUID

class UserNotFoundException(userId: UUID) :
    DomainException(
        code = "USER_NOT_FOUND",
        message = "User with id $userId not found"
    )