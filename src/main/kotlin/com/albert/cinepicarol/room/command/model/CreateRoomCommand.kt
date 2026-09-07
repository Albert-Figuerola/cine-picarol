package com.albert.cinepicarol.room.command.model

import com.albert.cinepicarol.room.domain.RoomType

data class CreateRoomCommand (
    val name: String,
    val type: RoomType,
    val capacity: Int
)