package com.albert.cinepicarol.auth.domain

import com.albert.cinepicarol.user.domain.User

data class LoginResult(
    val user: User,
    val token: String
)