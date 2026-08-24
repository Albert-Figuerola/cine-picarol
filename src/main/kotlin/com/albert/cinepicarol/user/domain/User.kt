package com.albert.cinepicarol.user.domain

import java.time.LocalDateTime
import java.util.UUID

data class User (
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)