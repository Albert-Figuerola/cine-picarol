package com.albert.cinepicarol.seat.entity

import com.albert.cinepicarol.common.persistence.AuditableEntity
import com.albert.cinepicarol.room.entity.RoomEntity
import com.albert.cinepicarol.seat.domain.SeatType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "seats")
class SeatEntity(

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,

    @Column(name = "row_letter", nullable = false, length = 1)
    val row: String,

    @Column(nullable = false)
    val number: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: SeatType,

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    val room: RoomEntity

) : AuditableEntity()
