package com.albert.cinepicarol.seat.mapper

import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.query.response.SeatResponse

internal fun Seat.toResponse() : SeatResponse =
    SeatResponse(
        id = id,
        row = row,
        number = number,
        type = type,
        roomId = roomId
    )