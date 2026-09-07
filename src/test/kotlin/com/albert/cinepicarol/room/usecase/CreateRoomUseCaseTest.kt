package com.albert.cinepicarol.room.usecase

import com.albert.cinepicarol.room.command.model.CreateRoomCommand
import com.albert.cinepicarol.room.command.usecase.CreateRoomUseCase
import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.domain.RoomType
import com.albert.cinepicarol.room.exception.RoomAlreadyExistsException
import com.albert.cinepicarol.room.port.RoomPort
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateRoomUseCaseTest {

    private val roomPort = mock<RoomPort>()
    private val createRoomUseCase = CreateRoomUseCase(roomPort)

    @Test
    fun `should create a room`() {
        val command = createRoomCommand()

        whenever(roomPort.existsByName(command.name))
            .thenReturn(false)

        whenever(roomPort.save(any<Room>()))
            .thenAnswer { it.arguments[0] as Room }

        val result = createRoomUseCase.execute(command)

        assertEquals(command.name, result.name)
        assertEquals(command.type, result.type)
        assertEquals(command.capacity, result.capacity)

        verify(roomPort).existsByName(command.name)
        verify(roomPort).save(any<Room>())
    }

    @Test
    fun `should throw RoomAlreadyExistsException when room name already exists`() {
        val command = createRoomCommand()

        whenever(roomPort.existsByName(command.name))
            .thenReturn(true)

        assertThrows<RoomAlreadyExistsException> {
            createRoomUseCase.execute(command)
        }

        verify(roomPort).existsByName(command.name)
        verify(roomPort, never()).save(any<Room>())
    }

    private fun createRoomCommand() = CreateRoomCommand(
        name = "Sala 1",
        type = RoomType.IMAX,
        capacity = 180
    )

}