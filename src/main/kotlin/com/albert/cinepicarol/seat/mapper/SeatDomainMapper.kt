package com.albert.cinepicarol.seat.mapper

import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.entity.SeatEntity

internal fun SeatEntity.toDomain(): Seat =
    Seat(
        id = id,
        row = row,
        number = number,
        type = type,
        roomId = room.id
    )