package com.albert.cinepicarol.user.command.mapper

import com.albert.cinepicarol.user.command.model.UpdateUserCommand
import com.albert.cinepicarol.user.command.request.UpdateUserRequest

internal fun UpdateUserRequest.toCommand() =
    UpdateUserCommand(
        firstName = firstName,
        lastName = lastName
    )