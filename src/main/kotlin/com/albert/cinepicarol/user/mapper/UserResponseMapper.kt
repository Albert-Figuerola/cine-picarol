package com.albert.cinepicarol.user.mapper

import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.query.response.UserResponse

internal fun User.toResponse() =
    UserResponse(
        id,
        firstName,
        lastName,
        email,
        role,
        createdAt,
        updatedAt
    )