package com.albert.cinepicarol.user.command.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateUserRequest(
    @field:NotBlank(message = "Firstname cannot be empty")
    val firstName: String,

    @field:NotBlank(message = "Lastname cannot be empty")
    val lastName: String,

    @field:NotBlank(message = "Email cannot be empty")
    @field:Email(message = "User email must be valid")
    val email: String,

    @field:NotBlank(message = "Password cannot be empty")
    val password: String
)