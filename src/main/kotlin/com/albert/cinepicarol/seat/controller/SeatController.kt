package com.albert.cinepicarol.seat.controller

import com.albert.cinepicarol.common.response.ApiResponse
import com.albert.cinepicarol.seat.command.mapper.toCommand
import com.albert.cinepicarol.seat.command.model.CreateSeatRequest
import com.albert.cinepicarol.seat.command.usecase.CreateSeatUseCase
import com.albert.cinepicarol.seat.mapper.toResponse
import com.albert.cinepicarol.seat.query.response.SeatResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/seats")
class SeatController(
    private val createSeatUseCase: CreateSeatUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSeat(
        @Valid @RequestBody request: CreateSeatRequest
    ) : ApiResponse<SeatResponse> {
        val seat = createSeatUseCase.execute(request.toCommand())
        return ApiResponse(
            data = seat.toResponse(),
        )
    }

}
