package com.albert.cinepicarol.room.mapper

import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.query.response.RoomResponse

internal fun Room.toResponse(): RoomResponse =
    RoomResponse(
        id = id,
        name = name,
        type = type,
        capacity = capacity
    )