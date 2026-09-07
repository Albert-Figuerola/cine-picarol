package com.albert.cinepicarol.room.repository

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.room.domain.RoomType
import com.albert.cinepicarol.room.entity.RoomEntity
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class RoomRepositoryTest {

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    fun `should save room`() {
        val room = RoomEntity(
            id = UUID.randomUUID(),
            name = "Sala ${UUID.randomUUID()}",
            type = RoomType.IMAX,
            capacity = 180,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )

        val savedRoom = roomRepository.save(room)
        val foundRoom = roomRepository.findByIdOrNull(savedRoom.id)
        val existsRoom = roomRepository.existsByName(savedRoom.name)

        assertNotNull(foundRoom)
        assertEquals(room.id, foundRoom.id)
        assertEquals(room.name, foundRoom.name)
        assertEquals(room.type, foundRoom.type)
        assertEquals(room.capacity, foundRoom.capacity)

        assertTrue(existsRoom)
    }

}