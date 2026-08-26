package com.albert.cinepicarol.auth.response

import com.albert.cinepicarol.user.domain.UserRole
import java.util.UUID

data class LoginResponse (
    val id: UUID,
    val email: String,
    val role: UserRole,
    val token: String
)