package com.albert.cinepicarol.user.command.model

data class CreateUserCommand (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)