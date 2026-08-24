package com.albert.cinepicarol.user.query.response

import com.albert.cinepicarol.user.domain.UserRole
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse (
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)