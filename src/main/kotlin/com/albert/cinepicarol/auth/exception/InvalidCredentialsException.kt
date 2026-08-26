package com.albert.cinepicarol.auth.exception

import com.albert.cinepicarol.common.exception.DomainException

class InvalidCredentialsException :
    DomainException(
        code = "INVALID_CREDENTIALS",
        message = "Invalid email or password"
    )