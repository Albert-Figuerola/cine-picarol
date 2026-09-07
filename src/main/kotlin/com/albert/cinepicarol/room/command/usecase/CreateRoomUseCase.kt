package com.albert.cinepicarol.room.command.usecase

import com.albert.cinepicarol.room.command.model.CreateRoomCommand
import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.exception.RoomAlreadyExistsException
import com.albert.cinepicarol.room.port.RoomPort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreateRoomUseCase(
    private val roomPort: RoomPort
) {

    fun execute(command: CreateRoomCommand): Room {
        if (roomPort.existsByName(command.name)) {
            throw RoomAlreadyExistsException(command.name)
        }

        val now = LocalDateTime.now()

        val room = Room(
            id = UUID.randomUUID(),
            name = command.name,
            type = command.type,
            capacity = command.capacity,
            createdAt = now,
            updatedAt = now
        )

        return roomPort.save(room)
    }
}