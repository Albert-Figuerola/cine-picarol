package com.albert.cinepicarol.room.mapper

import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.entity.RoomEntity

internal fun RoomEntity.toDomain(): Room =
        Room(
            id = id,
            name = name,
            type = type,
            capacity = capacity
        )