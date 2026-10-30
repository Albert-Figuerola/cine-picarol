package com.albert.cinepicarol.seat.adapter

import com.albert.cinepicarol.room.exception.RoomNotFoundException
import com.albert.cinepicarol.room.repository.RoomRepository
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.exception.SeatAlreadyExistsException
import com.albert.cinepicarol.seat.mapper.toDomain
import com.albert.cinepicarol.seat.mapper.toEntity
import com.albert.cinepicarol.seat.port.SeatPort
import com.albert.cinepicarol.seat.repository.SeatRepository
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

private const val SEAT_UNIQUE_CONSTRAINT = "uk_seats_room_row_number"
private const val SEATS_TABLE = "seats"

@Repository
class SeatJpaAdapter (
    private val seatRepository: SeatRepository,
    private val roomRepository: RoomRepository
) : SeatPort {

    override fun existsByRoomIdAndRowAndNumber(
        roomId: UUID,
        row: String,
        number: Int
    ): Boolean = seatRepository.existsByRoom_IdAndRowAndNumber(roomId, row, number)

    override fun save(seat: Seat): Seat {
        val room = roomRepository.findByIdOrNull(seat.roomId)
            ?: throw RoomNotFoundException(seat.roomId)

        try {
            return seatRepository.saveAndFlush(seat.toEntity(room)).toDomain()
        } catch (ex: DataIntegrityViolationException) {
            val constraintException = generateSequence(ex as Throwable?) { it.cause }
                .filterIsInstance<ConstraintViolationException>()
                .firstOrNull()

            val constraintName = constraintException?.constraintName

            if (
                constraintName == SEAT_UNIQUE_CONSTRAINT ||
                constraintName == "$SEATS_TABLE.$SEAT_UNIQUE_CONSTRAINT"
            ) {
                throw SeatAlreadyExistsException(seat.roomId, seat.row, seat.number)
            }

            throw ex
        }
    }

}