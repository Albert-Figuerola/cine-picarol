package com.albert.cinepicarol.seat.port

import com.albert.cinepicarol.seat.domain.Seat

interface SeatPort {

    fun save(seat: Seat): Seat

}