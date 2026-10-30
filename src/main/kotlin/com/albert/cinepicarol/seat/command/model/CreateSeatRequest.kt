package com.albert.cinepicarol.seat.command.model

import com.albert.cinepicarol.seat.domain.SeatType
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import java.util.UUID

data class CreateSeatRequest (
    @field:Pattern(regexp = "[A-Z]", message = "Row must be a single uppercase letter from A to Z")
    val row: String,

    @field:Min(value = 1, message = "Seat number must be at least 1")
    val number: Int,

    val type: SeatType,

    val roomId: UUID
)
