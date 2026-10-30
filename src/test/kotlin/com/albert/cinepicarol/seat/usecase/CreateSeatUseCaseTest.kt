package com.albert.cinepicarol.seat.usecase

import com.albert.cinepicarol.seat.command.model.CreateSeatCommand
import com.albert.cinepicarol.seat.command.usecase.CreateSeatUseCase
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.domain.SeatType
import com.albert.cinepicarol.seat.exception.SeatAlreadyExistsException
import com.albert.cinepicarol.seat.port.SeatPort
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID
import kotlin.test.assertEquals

class CreateSeatUseCaseTest {

    private val seatPort: SeatPort = mock()
    private val createSeatUseCase: CreateSeatUseCase = CreateSeatUseCase(seatPort)

    @Test
    fun `should create seat successfully`() {
        val command = createSeatCommand()

        whenever(
            seatPort.existsByRoomIdAndRowAndNumber(
                command.roomId, command.row, command.number
            )
        ).thenReturn(false)

        whenever(seatPort.save(any<Seat>())).then { it.arguments[0] as Seat }

        val result = createSeatUseCase.execute(command)

        assertEquals(command.row, result.row)
        assertEquals(command.number, result.number)
        assertEquals(command.type, result.type)
        assertEquals(command.roomId, result.roomId)

        verify(seatPort).existsByRoomIdAndRowAndNumber(
            command.roomId, command.row, command.number
        )
        verify(seatPort).save(any<Seat>())
    }

    @Test
    fun `should throw exception when seat already exists`() {
        val command = createSeatCommand()

        whenever(
            seatPort.existsByRoomIdAndRowAndNumber(
                command.roomId, command.row, command.number
            )
        ).thenReturn(true)

        assertThrows<SeatAlreadyExistsException> { createSeatUseCase.execute(command) }

        verify(seatPort).existsByRoomIdAndRowAndNumber(command.roomId, command.row, command.number)
        verify(seatPort, never()).save(any<Seat>())
    }

    private fun createSeatCommand() = CreateSeatCommand(
        row = "A", number = 1, type = SeatType.VIP, roomId = UUID.randomUUID()
    )

}