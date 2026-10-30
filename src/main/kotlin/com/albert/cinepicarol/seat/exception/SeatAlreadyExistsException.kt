package com.albert.cinepicarol.seat.exception

import com.albert.cinepicarol.common.exception.DomainException
import java.util.UUID

class SeatAlreadyExistsException(
    roomId: UUID,
    row: String,
    number: Int
) : DomainException(
    code = "SEAT_ALREADY_EXISTS",
    message = "Seat $row$number already exists in room $roomId"
)