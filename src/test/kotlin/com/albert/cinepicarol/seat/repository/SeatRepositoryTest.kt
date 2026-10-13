package com.albert.cinepicarol.seat.repository

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.room.domain.RoomType
import com.albert.cinepicarol.room.entity.RoomEntity
import com.albert.cinepicarol.room.repository.RoomRepository
import com.albert.cinepicarol.seat.domain.SeatType
import com.albert.cinepicarol.seat.entity.SeatEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class SeatRepositoryTest {

    @Autowired
    private lateinit var seatRepository: SeatRepository

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should save seat`() {
        val room = roomRepository.saveAndFlush(
            RoomEntity(
                id = UUID.randomUUID(),
                name = "Sala ${UUID.randomUUID()}",
                type = RoomType.IMAX,
                capacity = 180
            )
        )

        val seat = SeatEntity(
            id = UUID.randomUUID(),
            row = "A",
            number = 1,
            type = SeatType.STANDARD,
            room = room
        )

        val savedSeat = seatRepository.save(seat)
        val foundSeat = seatRepository.findByIdOrNull(savedSeat.id)

        assertNotNull(foundSeat)
        assertEquals(seat.id, foundSeat.id)
        assertEquals(seat.row, foundSeat.row)
        assertEquals(seat.number, foundSeat.number)
        assertEquals(seat.type, foundSeat.type)
        assertEquals(room.id, foundSeat.room.id)
        assertNotNull(savedSeat.createdAt)
        assertNotNull(savedSeat.updatedAt)
    }

    @Test
    fun `should not save duplicate seat in same room`() {
        val room = roomRepository.saveAndFlush(
            RoomEntity(
                id = UUID.randomUUID(),
                name = "Sala ${UUID.randomUUID()}",
                type = RoomType.IMAX,
                capacity = 180
            )
        )

        val seat = SeatEntity(
            id = UUID.randomUUID(),
            row = "B",
            number = 1,
            type = SeatType.STANDARD,
            room = room
        )

        seatRepository.saveAndFlush(seat)

        val duplicateSeat = SeatEntity(
            id = UUID.randomUUID(),
            row = "B",
            number = 1,
            type = SeatType.STANDARD,
            room = room
        )

        assertThrows<DataIntegrityViolationException> {
            seatRepository.saveAndFlush(duplicateSeat)
        }
    }

    @Test
    fun `should allow same seat number in different rooms`() {
        val imaxRoom = roomRepository.saveAndFlush(
            RoomEntity(
                id = UUID.randomUUID(),
                name = "Sala ${UUID.randomUUID()}",
                type = RoomType.IMAX,
                capacity = 180
            )
        )

        val imaxSeat = SeatEntity(
            id = UUID.randomUUID(),
            row = "C",
            number = 1,
            type = SeatType.STANDARD,
            room = imaxRoom
        )

        seatRepository.saveAndFlush(imaxSeat)

        val isenseRoom = roomRepository.saveAndFlush(
            RoomEntity(
                id = UUID.randomUUID(),
                name = "Sala ${UUID.randomUUID()}",
                type = RoomType.ISENSE,
                capacity = 180
            )
        )

        val isenseSeat = SeatEntity(
            id = UUID.randomUUID(),
            row = "C",
            number = 1,
            type = SeatType.STANDARD,
            room = isenseRoom
        )

        seatRepository.saveAndFlush(isenseSeat)

        val foundImaxSeat = seatRepository.findByIdOrNull(imaxSeat.id)
        val foundIsenseSeat = seatRepository.findByIdOrNull(isenseSeat.id)

        assertNotNull(foundImaxSeat)
        assertNotNull(foundIsenseSeat)

        assertEquals(imaxSeat.id, foundImaxSeat.id)
        assertEquals(isenseSeat.id, foundIsenseSeat.id)

        assertEquals(imaxRoom.id, foundImaxSeat.room.id)
        assertEquals(isenseRoom.id, foundIsenseSeat.room.id)

    }

}