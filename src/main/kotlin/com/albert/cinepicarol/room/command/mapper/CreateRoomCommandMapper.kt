package com.albert.cinepicarol.room.command.mapper

import com.albert.cinepicarol.room.command.model.CreateRoomCommand
import com.albert.cinepicarol.room.command.model.CreateRoomRequest

internal fun CreateRoomRequest.toCommand() =
    CreateRoomCommand(
        name = name,
        type = type,
        capacity = capacity
    )