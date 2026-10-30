package com.albert.cinepicarol.seat.port

import com.albert.cinepicarol.seat.domain.Seat
import java.util.UUID

interface SeatPort {

    fun existsByRoomIdAndRowAndNumber(
        roomId: UUID,
        row: String,
        number: Int
    ): Boolean

    fun save(seat: Seat): Seat

}