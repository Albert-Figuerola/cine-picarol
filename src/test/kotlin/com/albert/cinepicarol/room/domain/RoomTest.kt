package com.albert.cinepicarol.room.domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

class RoomTest {

    @Test
    fun `should create room when data is valid`() {
        val room = Room(
            id = UUID.randomUUID(),
            name = "Sala IMAX",
            type = RoomType.IMAX,
            capacity = 180,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )

        assertEquals("Sala IMAX", room.name)
        assertEquals(RoomType.IMAX, room.type)
        assertEquals(180, room.capacity)

    }

    @Test
    fun `should throw exception when room name is blank`() {
        assertThrows<IllegalArgumentException> {
            Room(
                id = UUID.randomUUID(),
                name = "    ",
                type = RoomType.ISENSE,
                capacity = 180,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        }
    }

    @Test
    fun `should throw exception when room capacity is not positive`() {
        assertThrows<IllegalArgumentException> {
            Room(
                id = UUID.randomUUID(),
                name = "Titanic",
                type = RoomType.ISENSE,
                capacity = 0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        }
    }

}