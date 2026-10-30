package com.albert.cinepicarol.seat.controller

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.auth.security.JwtAuthenticationFilter
import com.albert.cinepicarol.config.SecurityConfig
import com.albert.cinepicarol.seat.command.model.CreateSeatCommand
import com.albert.cinepicarol.seat.command.usecase.CreateSeatUseCase
import com.albert.cinepicarol.seat.domain.Seat
import com.albert.cinepicarol.seat.domain.SeatType
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.springframework.http.MediaType
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(SeatController::class)
@Import(
    SecurityConfig::class,
    JwtAuthenticationFilter::class
)
class SeatControllerSecurityTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createSeatUseCase: CreateSeatUseCase

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    @WithAnonymousUser
    fun `should return 401 when creating seat without authentication`() {
        mockMvc.perform(
            post("/api/v1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateSeatRequest())
        )
            .andExpect(status().isUnauthorized)

        verifyNoInteractions(createSeatUseCase)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should return 403 when USER tries to create seat`() {
        mockMvc.perform(
            post("/api/v1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateSeatRequest())
        )
            .andExpect(status().isForbidden)

        verifyNoInteractions(createSeatUseCase)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return 201 when ADMIN creates seat`() {
        val roomId = UUID.randomUUID()

        whenever(createSeatUseCase.execute(any<CreateSeatCommand>()))
            .thenReturn(createSeat(roomId))

        mockMvc.perform(
            post("/api/v1/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateSeatRequest(roomId))
        )
            .andExpect(status().isCreated)

        verify(createSeatUseCase).execute(any<CreateSeatCommand>())
    }

    private fun createSeat(roomId: UUID) = Seat(
        id = UUID.randomUUID(),
        row = "A",
        number = 1,
        type = SeatType.VIP,
        roomId = roomId
    )

    private fun validCreateSeatRequest(
        roomId: UUID = UUID.randomUUID()
    ) = """
        {
            "row": "A",
            "number": 1,
            "type": "VIP",
            "roomId": "$roomId"
        }
    """.trimIndent()

}