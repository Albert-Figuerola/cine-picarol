package com.albert.cinepicarol.seat.command.usecase

import com.albert.cinepicarol.seat.command.model.CreateSeatCommand
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.exception.SeatAlreadyExistsException
import com.albert.cinepicarol.seat.port.SeatPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CreateSeatUseCase(
    private val seatPort: SeatPort
) {

    fun execute(command: CreateSeatCommand): Seat {

        if (seatPort.existsByRoomIdAndRowAndNumber(command.roomId, command.row, command.number)) {
            throw SeatAlreadyExistsException(command.roomId, command.row, command.number)
        }

        val seat = Seat(
            id = UUID.randomUUID(),
            row = command.row,
            number = command.number,
            type = command.type,
            roomId = command.roomId
        )

        return seatPort.save(seat)
    }

}