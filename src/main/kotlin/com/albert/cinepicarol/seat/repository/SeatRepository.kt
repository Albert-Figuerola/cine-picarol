package com.albert.cinepicarol.seat.repository

import com.albert.cinepicarol.seat.entity.SeatEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SeatRepository : JpaRepository<SeatEntity, UUID> {
}