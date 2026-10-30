package com.albert.cinepicarol.seat.command.model

import com.albert.cinepicarol.seat.domain.SeatType
import java.util.UUID

data class CreateSeatCommand(
    val row: String,
    val number: Int,
    val type: SeatType,
    val roomId: UUID
)