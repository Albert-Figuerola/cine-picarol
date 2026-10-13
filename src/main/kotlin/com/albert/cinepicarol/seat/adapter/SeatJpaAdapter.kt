package com.albert.cinepicarol.seat.adapter

import com.albert.cinepicarol.room.exception.RoomNotFoundException
import com.albert.cinepicarol.room.repository.RoomRepository
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.mapper.toDomain
import com.albert.cinepicarol.seat.mapper.toEntity
import com.albert.cinepicarol.seat.port.SeatPort
import com.albert.cinepicarol.seat.repository.SeatRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class SeatJpaAdapter (
    private val seatRepository: SeatRepository,
    private val roomRepository: RoomRepository
) : SeatPort {

    override fun save(seat: Seat): Seat {
        val room = roomRepository.findByIdOrNull(seat.roomId)
            ?: throw RoomNotFoundException(seat.roomId)

        return seatRepository.save(seat.toEntity(room)).toDomain()
    }

}