package com.albert.cinepicarol.room.adapter

import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.mapper.toDomain
import com.albert.cinepicarol.room.mapper.toEntity
import com.albert.cinepicarol.room.port.RoomPort
import com.albert.cinepicarol.room.repository.RoomRepository
import org.springframework.stereotype.Repository

@Repository
class RoomJpaAdapter (
    private val roomRepository: RoomRepository
) : RoomPort {

    override fun existsByName(name: String): Boolean {
        return roomRepository.existsByName(name)
    }

    override fun save(room: Room): Room {
        return roomRepository.save(room.toEntity()).toDomain()
    }
}