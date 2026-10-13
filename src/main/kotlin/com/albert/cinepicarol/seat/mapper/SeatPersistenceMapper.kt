package com.albert.cinepicarol.seat.mapper

import com.albert.cinepicarol.room.entity.RoomEntity
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.entity.SeatEntity

internal fun Seat.toEntity(roomEntity: RoomEntity) : SeatEntity =
    SeatEntity(
        id = id,
        row = row,
        number = number,
        type = type,
        room = roomEntity
    )