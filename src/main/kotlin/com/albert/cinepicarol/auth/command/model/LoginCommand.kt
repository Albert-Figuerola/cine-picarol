package com.albert.cinepicarol.auth.command.model

data class LoginCommand(
    val email: String,
    val password: String
)