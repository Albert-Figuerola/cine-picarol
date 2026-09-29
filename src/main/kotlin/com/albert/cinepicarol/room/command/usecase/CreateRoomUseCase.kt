package com.albert.cinepicarol.room.command.usecase

import com.albert.cinepicarol.room.command.model.CreateRoomCommand
import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.exception.RoomAlreadyExistsException
import com.albert.cinepicarol.room.port.RoomPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CreateRoomUseCase(
    private val roomPort: RoomPort
) {

    fun execute(command: CreateRoomCommand): Room {
        if (roomPort.existsByName(command.name)) {
            throw RoomAlreadyExistsException(command.name)
        }

        val room = Room(
            id = UUID.randomUUID(),
            name = command.name,
            type = command.type,
            capacity = command.capacity
        )

        return roomPort.save(room)
    }
}