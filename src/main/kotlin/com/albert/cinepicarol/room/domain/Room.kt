package com.albert.cinepicarol.room.domain

import java.util.UUID

data class Room (
    val id: UUID,
    val name: String,
    val type: RoomType,
    val capacity: Int
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(capacity > 0) { "Capacity must be greater than zero." }
    }
}