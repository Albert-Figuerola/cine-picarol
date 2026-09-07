package com.albert.cinepicarol.room.repository

import com.albert.cinepicarol.room.entity.RoomEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoomRepository : JpaRepository<RoomEntity, UUID> {

    fun existsByName(name: String): Boolean

}