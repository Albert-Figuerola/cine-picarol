package com.albert.cinepicarol.seat.query.response

import com.albert.cinepicarol.seat.domain.SeatType
import java.util.UUID

data class SeatResponse (
    val id : UUID,
    val row: String,
    val number: Int,
    val type: SeatType,
    val roomId: UUID
)