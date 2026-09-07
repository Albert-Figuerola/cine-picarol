package com.albert.cinepicarol.room.domain

import java.time.LocalDateTime
import java.util.UUID

data class Room (
    val id: UUID,
    val name: String,
    val type: RoomType,
    val capacity: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(capacity > 0) { "Capacity must be greater than zero." }
    }
}