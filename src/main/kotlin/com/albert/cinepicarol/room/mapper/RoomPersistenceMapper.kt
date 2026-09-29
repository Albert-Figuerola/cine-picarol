package com.albert.cinepicarol.room.mapper

import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.entity.RoomEntity

internal fun Room.toEntity(): RoomEntity =
    RoomEntity(
        id = id,
        name = name,
        type = type,
        capacity = capacity
    )