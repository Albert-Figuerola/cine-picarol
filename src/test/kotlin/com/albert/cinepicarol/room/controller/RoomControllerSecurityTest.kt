package com.albert.cinepicarol.room.controller

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.auth.security.JwtAuthenticationFilter
import com.albert.cinepicarol.config.SecurityConfig
import com.albert.cinepicarol.room.command.usecase.CreateRoomUseCase
import com.albert.cinepicarol.room.domain.Room
import com.albert.cinepicarol.room.domain.RoomType
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(RoomController::class)
@Import(
    SecurityConfig::class,
    JwtAuthenticationFilter::class
)
class RoomControllerSecurityTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createRoomUseCase: CreateRoomUseCase

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return 201 when ADMIN create a room`() {
        whenever(createRoomUseCase.execute(any()))
            .thenReturn(createRoom())

        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateRoomRequest())
        )
            .andExpect(status().isCreated)
    }

    @Test
    @WithAnonymousUser
    fun `should return 401 when getting movies without authentication`() {
        mockMvc.perform(
            post("/api/v1/rooms")
        )
            .andExpect(status().isUnauthorized)

        verifyNoInteractions(createRoomUseCase)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should return 403 when USER creates a room`() {
        mockMvc.perform(
            post("/api/v1/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateRoomRequest())
        )
            .andExpect(status().isForbidden)

        verifyNoInteractions(createRoomUseCase)
    }

    private fun createRoom() = Room(
        id = UUID.randomUUID(),
        name = "Sala 1",
        type = RoomType.IMAX,
        capacity = 180
    )

    private fun validCreateRoomRequest() = """
        {
            "name": "Sala 1",
            "type": "IMAX",
            "capacity": 180
        }
    """.trimIndent()

}