package com.albert.cinepicarol.room.repository

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.room.domain.RoomType
import com.albert.cinepicarol.room.entity.RoomEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class RoomRepositoryTest {

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should save room`() {
        val room = RoomEntity(
            id = UUID.randomUUID(),
            name = "Sala ${UUID.randomUUID()}",
            type = RoomType.IMAX,
            capacity = 180
        )

        val savedRoom = roomRepository.save(room)
        val foundRoom = roomRepository.findByIdOrNull(savedRoom.id)
        val existsRoom = roomRepository.existsByName(savedRoom.name)

        assertNotNull(foundRoom)
        assertEquals(room.id, foundRoom.id)
        assertEquals(room.name, foundRoom.name)
        assertEquals(room.type, foundRoom.type)
        assertEquals(room.capacity, foundRoom.capacity)
        assertNotNull(savedRoom.createdAt)
        assertNotNull(savedRoom.updatedAt)

        assertTrue(existsRoom)
    }

    @Test
    fun `should update updatedAt when room is modified`() {
        val room = RoomEntity(
            id = UUID.randomUUID(),
            name = "Sala ${UUID.randomUUID()}",
            type = RoomType.IMAX,
            capacity = 180
        )

        val savedRoom = roomRepository.saveAndFlush(room)

        val createdAt = savedRoom.createdAt
        val updatedAt = savedRoom.updatedAt

        Thread.sleep(10)

        savedRoom.capacity = 200

        val updatedRoom = roomRepository.saveAndFlush(savedRoom)

        assertEquals(createdAt, updatedRoom.createdAt)
        assertTrue(updatedRoom.updatedAt.isAfter(updatedAt))
    }

    @Test
    fun `should audit authenticated user`() {
        val userId = UUID.randomUUID()

        val authentication = UsernamePasswordAuthenticationToken(
            userId,
            null,
            emptyList()
        )

        SecurityContextHolder.getContext().authentication = authentication

        val room = RoomEntity(
            id = UUID.randomUUID(),
            name = "Sala ${UUID.randomUUID()}",
            type = RoomType.IMAX,
            capacity = 180
        )

        val savedRoom = roomRepository.saveAndFlush(room)

        assertEquals(userId, savedRoom.createdById)
        assertEquals(userId, savedRoom.updatedById)
    }

    @Test
    fun `should audit user when room is updated`() {
        val createdByUserId = UUID.randomUUID()

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                createdByUserId,
                null,
                emptyList()
            )

        val room = RoomEntity(
            id = UUID.randomUUID(),
            name = "Sala ${UUID.randomUUID()}",
            type = RoomType.IMAX,
            capacity = 180
        )

        val savedRoom = roomRepository.saveAndFlush(room)

        val updatedByUserId = UUID.randomUUID()

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                updatedByUserId,
                null,
                emptyList()
            )

        savedRoom.name = "Sala - ${UUID.randomUUID()}"
        savedRoom.type = RoomType.ISENSE

        val updatedRoom = roomRepository.saveAndFlush(savedRoom)

        assertEquals(createdByUserId, updatedRoom.createdById)
        assertEquals(updatedByUserId, updatedRoom.updatedById)
    }

}