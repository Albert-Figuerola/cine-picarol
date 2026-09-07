package com.albert.cinepicarol.room.command.model

import com.albert.cinepicarol.room.domain.RoomType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class CreateRoomRequest (
    @field:NotBlank(message = "Room name cannot be empty")
    val name: String,

    val type: RoomType,

    @field:Positive(message = "Room capacity must be greater than zero")
    val capacity: Int
)