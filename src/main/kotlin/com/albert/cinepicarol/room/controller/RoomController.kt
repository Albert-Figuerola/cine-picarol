package com.albert.cinepicarol.room.controller

import com.albert.cinepicarol.common.response.ApiResponse
import com.albert.cinepicarol.room.command.mapper.toCommand
import com.albert.cinepicarol.room.command.model.CreateRoomRequest
import com.albert.cinepicarol.room.command.usecase.CreateRoomUseCase
import com.albert.cinepicarol.room.mapper.toResponse
import com.albert.cinepicarol.room.query.response.RoomResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/rooms")
class RoomController (
    private val createRoomUseCase: CreateRoomUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createRoom(
        @Valid @RequestBody request: CreateRoomRequest
    ): ApiResponse<RoomResponse> {
        val room = createRoomUseCase.execute(request.toCommand())
        return ApiResponse(
            data = room.toResponse()
        )
    }

}