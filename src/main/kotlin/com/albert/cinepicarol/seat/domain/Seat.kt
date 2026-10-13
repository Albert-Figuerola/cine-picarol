package com.albert.cinepicarol.seat.domain

import java.util.UUID

data class Seat(
    val id: UUID,
    val row: String,
    val number: Int,
    val type: SeatType,
    val roomId: UUID
)
