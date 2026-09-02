package com.albert.cinepicarol.user.command.request

import jakarta.validation.constraints.Pattern


data class UpdateUserRequest (
    @field:Pattern(
        regexp = ".*\\S.*",
        message = "Firstname cannot be empty"
    )
    val firstName: String?,

    @field:Pattern(
        regexp = ".*\\S.*",
        message = "Lastname cannot be empty"
    )
    val lastName: String?,
)