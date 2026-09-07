package com.albert.cinepicarol.room.exception

import com.albert.cinepicarol.common.exception.DomainException

class RoomAlreadyExistsException(
    name: String
) : DomainException (
    code = "ROOM_ALREADY_EXISTS",
    message = "Room $name already exists"
)