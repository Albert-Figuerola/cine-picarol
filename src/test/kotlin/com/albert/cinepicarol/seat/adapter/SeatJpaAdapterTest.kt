package com.albert.cinepicarol.seat.adapter

import com.albert.cinepicarol.room.domain.RoomType
import com.albert.cinepicarol.room.entity.RoomEntity
import com.albert.cinepicarol.room.exception.RoomNotFoundException
import com.albert.cinepicarol.room.repository.RoomRepository
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.domain.SeatType
import com.albert.cinepicarol.seat.entity.SeatEntity
import com.albert.cinepicarol.seat.repository.SeatRepository
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.util.Optional
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class SeatJpaAdapterTest {

    private val seatRepository: SeatRepository = mock()
    private val roomRepository: RoomRepository = mock()

    private val seatJpaAdapter = SeatJpaAdapter(
        seatRepository = seatRepository,
        roomRepository = roomRepository
    )

    @Test
    fun `should throw RoomNotFoundException when room does not exist`() {
        val seat = Seat(
            id = UUID.randomUUID(),
            row = "C",
            number = 1,
            type = SeatType.STANDARD,
            roomId = UUID.randomUUID()
        )

        whenever(roomRepository.findById(seat.roomId))
            .thenReturn(Optional.empty())

        assertThrows<RoomNotFoundException> {
            seatJpaAdapter.save(seat)
        }

        verifyNoInteractions(seatRepository)
    }

    @Test
    fun `should save seat when room exists`() {
        val room = RoomEntity(
            id = UUID.randomUUID(),
            name = "Room 1",
            type = RoomType.IMAX,
            capacity = 100,
        )

        val seat = Seat(
            id = UUID.randomUUID(),
            row = "C",
            number = 1,
            type = SeatType.STANDARD,
            roomId = room.id,
        )

        val savedEntity = SeatEntity(
            id = seat.id,
            row = seat.row,
            number = seat.number,
            type = seat.type,
            room = room,
        )

        whenever(roomRepository.findById(room.id)).thenReturn(Optional.of(room))
        whenever(seatRepository.save(any<SeatEntity>())).thenReturn(savedEntity)

        val result = seatJpaAdapter.save(seat)

        val entityCaptor = argumentCaptor<SeatEntity>()

        verify(roomRepository).findById(room.id)
        verify(seatRepository).save(entityCaptor.capture())
        with(entityCaptor.firstValue) {
            assertEquals(seat.id, id)
            assertEquals(seat.row, row)
            assertEquals(seat.number, number)
            assertEquals(seat.type, type)
            assertSame(room, this.room)
        }
        assertEquals(seat, result)
    }

}
