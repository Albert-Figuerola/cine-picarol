package com.albert.cinepicarol.user.command.mapper

import com.albert.cinepicarol.user.command.model.CreateUserCommand
import com.albert.cinepicarol.user.command.request.CreateUserRequest

internal fun CreateUserRequest.toCommand() =
    CreateUserCommand(
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = password
    )