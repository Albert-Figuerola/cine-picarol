package com.albert.cinepicarol.auth.mapper

import com.albert.cinepicarol.auth.domain.LoginResult
import com.albert.cinepicarol.auth.response.LoginResponse

internal fun LoginResult.toResponse() =
    LoginResponse(
        id = user.id,
        email = user.email,
        role = user.role,
        token = token
    )