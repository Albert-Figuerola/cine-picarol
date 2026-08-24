package com.albert.cinepicarol.user.exception

import com.albert.cinepicarol.common.exception.DomainException

class UserAlreadyExistsException (
    email: String
) : DomainException (
    code = "USER_ALREADY_EXISTS",
    message = "User $email already exists"
)