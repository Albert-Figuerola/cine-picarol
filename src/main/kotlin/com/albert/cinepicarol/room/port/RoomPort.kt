package com.albert.cinepicarol.room.port

import com.albert.cinepicarol.room.domain.Room

interface RoomPort {

    fun existsByName(name: String): Boolean

    fun save(room: Room): Room

}