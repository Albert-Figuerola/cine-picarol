package com.albert.cinepicarol.user.mapper

import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.entity.UserEntity

internal fun UserEntity.toDomain(): User =
    User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        passwordHash = passwordHash,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )