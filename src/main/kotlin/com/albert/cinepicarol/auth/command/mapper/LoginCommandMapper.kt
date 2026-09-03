package com.albert.cinepicarol.auth.command.mapper

import com.albert.cinepicarol.auth.command.model.LoginCommand
import com.albert.cinepicarol.auth.command.request.LoginRequest

internal fun LoginRequest.toCommand() =
    LoginCommand(
        email = email,
        password = password
    )