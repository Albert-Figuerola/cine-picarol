package com.albert.cinepicarol.room.controller

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.room.command.model.CreateRoomCommand
import com.albert.cinepicarol.room.command.model.CreateRoomRequest
import com.albert.cinepicarol.room.command.usecase.CreateRoomUseCase
import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.domain.RoomType
import com.albert.cinepicarol.room.exception.RoomAlreadyExistsException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
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

@WebMvcTest(RoomController::class)
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var createRoomUseCase: CreateRoomUseCase

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    fun `should return 201 when a room is created`() {
        val room = createRoom()

        whenever(createRoomUseCase.execute(any<CreateRoomCommand>()))
            .thenReturn(room)

        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(createRoomRequest())
                )
        )
            .andExpect(status().isCreated)

            .andExpect(jsonPath("$.data.id").value(room.id.toString()))
            .andExpect(jsonPath("$.data.name").value(room.name))
            .andExpect(jsonPath("$.data.type").value(room.type.name))
            .andExpect(jsonPath("$.data.capacity").value(room.capacity))

        verify(createRoomUseCase).execute(any<CreateRoomCommand>())
    }

    @Test
    fun `should return 409 when there already exists a room with the same name`() {
        val request = createRoomRequest()

        doThrow(RoomAlreadyExistsException(request.name))
            .whenever(createRoomUseCase)
            .execute(any<CreateRoomCommand>())

        mockMvc.perform(
            post("/api/v1/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("ROOM_ALREADY_EXISTS"))
            .andExpect(jsonPath("$.message").value("Room ${request.name} already exists"))
    }

    @Test
    fun `should return 400 when name is an empty string`() {
        val request = createRoomRequest().copy(name = "")

        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Room name cannot be empty"))
    }

    @Test
    fun `should return 400 when name blank`() {
        val request = createRoomRequest().copy(name = "  ")

        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Room name cannot be empty"))
    }

    @Test
    fun `should return 400 when capacity is zero`() {
        val request = createRoomRequest().copy(capacity = 0)

        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Room capacity must be greater than zero"))
    }

    @Test
    fun `should return 400 when capacity is less than zero`() {
        val request = createRoomRequest().copy(capacity = 0)

        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Room capacity must be greater than zero"))
    }

    @Test
    fun `should return 400 when type is invalid`() {
        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                    "name": "Sala IMAX",
                    "type": "TEST",
                    "capacity": 180
                }
                """.trimIndent()
                )
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
            .andExpect(jsonPath("$.message").value("Request body is invalid"))
    }

    private fun createRoom(): Room {
        return Room(
            id = UUID.randomUUID(),
            name = "Sala IMAX",
            type = RoomType.IMAX,
            capacity = 180
        )
    }

    private fun createRoomRequest() = CreateRoomRequest(
        name = "Sala IMAX",
        type = RoomType.IMAX,
        capacity = 180
    )

}