package com.albert.cinepicarol.user.mapper

import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.entity.UserEntity

internal fun User.toEntity(): UserEntity =
    UserEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        passwordHash = passwordHash,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

