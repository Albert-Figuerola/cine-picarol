package com.albert.cinepicarol.seat.controller

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.seat.command.model.CreateSeatCommand
import com.albert.cinepicarol.seat.command.model.CreateSeatRequest
import com.albert.cinepicarol.seat.command.usecase.CreateSeatUseCase
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.domain.SeatType
import com.albert.cinepicarol.seat.exception.SeatAlreadyExistsException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(SeatController::class)
@AutoConfigureMockMvc(addFilters = false)
class SeatControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var createSeatUseCase: CreateSeatUseCase

    @MockitoBean
    private lateinit var tokenPort: TokenPort


    @Test
    fun `should return 201 when a seat is created`() {
        val seat = createSeat()

        whenever(createSeatUseCase.execute(any<CreateSeatCommand>()))
            .thenReturn(seat)

        mockMvc.perform(
            post("/api/v1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSeatRequest()))
        )
            .andExpect(status().isCreated)

            .andExpect(jsonPath("$.data.id").value(seat.id.toString()))
            .andExpect(jsonPath("$.data.row").value(seat.row))
            .andExpect(jsonPath("$.data.number").value(seat.number))
            .andExpect(jsonPath("$.data.type").value(seat.type.name))
            .andExpect(jsonPath("$.data.roomId").value(seat.roomId.toString()))

        verify(createSeatUseCase).execute(any<CreateSeatCommand>())
    }

    @Test
    fun `should return 400 when seat row is invalid`() {
        val request = createSeatRequest().copy(row = "AA")

        mockMvc.perform(
            post("/api/v1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)

            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Row must be a single uppercase letter from A to Z"))

        verify(createSeatUseCase, never()).execute(any<CreateSeatCommand>())
    }

    @Test
    fun `should return 400 when seat number is invalid`() {
        val request = createSeatRequest().copy(number = -12)

        mockMvc.perform(
            post("/api/v1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)

            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Seat number must be at least 1"))

        verify(createSeatUseCase, never()).execute(any<CreateSeatCommand>())
    }

    @Test
    fun `should return 409 when seat already exists`() {
        val request = createSeatRequest()

        whenever(createSeatUseCase.execute(any<CreateSeatCommand>()))
            .thenThrow(SeatAlreadyExistsException(request.roomId, request.row, request.number))

        mockMvc.perform(
            post("/api/v1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("SEAT_ALREADY_EXISTS"))
            .andExpect(jsonPath("$.message").value("Seat ${request.row}${request.number} already exists in room ${request.roomId}"))
    }

    private fun createSeat() = Seat(
        id = UUID.randomUUID(),
        row = "A",
        number = 1,
        type = SeatType.VIP,
        roomId = UUID.randomUUID()
    )

    private fun createSeatRequest() = CreateSeatRequest(
        row = "A",
        number = 1,
        type = SeatType.VIP,
        roomId = UUID.randomUUID()
    )

}