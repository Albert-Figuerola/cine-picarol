package com.albert.cinepicarol.seat.command.mapper

import com.albert.cinepicarol.seat.command.model.CreateSeatCommand
import com.albert.cinepicarol.seat.command.model.CreateSeatRequest
import com.albert.cinepicarol.seat.domain.SeatType

internal fun CreateSeatRequest.toCommand() =
    CreateSeatCommand(
        row = row,
        number = number,
        type = SeatType.valueOf(type.name),
        roomId = roomId
    )