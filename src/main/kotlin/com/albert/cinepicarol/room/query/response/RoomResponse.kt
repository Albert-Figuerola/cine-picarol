package com.albert.cinepicarol.room.query.response

import com.albert.cinepicarol.room.domain.RoomType
import java.util.UUID

data class RoomResponse(
    val id: UUID,
    val name: String,
    val type: RoomType,
    val capacity: Int
)