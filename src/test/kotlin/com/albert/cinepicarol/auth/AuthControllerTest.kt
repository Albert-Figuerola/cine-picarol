package com.albert.cinepicarol.auth

import com.albert.cinepicarol.auth.command.request.LoginRequest
import com.albert.cinepicarol.auth.command.usecase.LoginUseCase
import com.albert.cinepicarol.auth.controller.AuthController
import com.albert.cinepicarol.auth.domain.LoginResult
import com.albert.cinepicarol.auth.exception.InvalidCredentialsException
import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.verify
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
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var loginUseCase: LoginUseCase

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    fun `should return 200 when credentials are valid`() {
        val request = loginRequest()
        val user = user()
        val token = "generated-token"
        val loginResult = LoginResult(user, token)

        whenever(loginUseCase.execute(any<LoginRequest>())).thenReturn(loginResult)

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value(user.id.toString()))
            .andExpect(jsonPath("$.data.email").value(user.email))
            .andExpect(jsonPath("$.data.role").value("USER"))
            .andExpect(jsonPath("$.data.token").value(token))

        verify(loginUseCase).execute(any<LoginRequest>())
    }

    @Test
    fun `should return 401 when credentials are invalid`() {
        val request = loginRequest()

        doThrow(InvalidCredentialsException())
            .whenever(loginUseCase)
            .execute(any<LoginRequest>())

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"))
            .andExpect(jsonPath("$.message").value("Invalid email or password"))

        verify(loginUseCase).execute(any<LoginRequest>())
    }

    @Test
    fun `should return 400 when email is invalid`() {
        val request = loginRequest().copy(email = "invalid-email")

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))

        verifyNoInteractions(loginUseCase)
    }

    @Test
    fun `should return 400 when password is blank`() {
        val request = loginRequest().copy(password = "")

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))

        verifyNoInteractions(loginUseCase)
    }

    private fun loginRequest() = LoginRequest(
        email = "amartinez@gmail.com",
        password = "M@rtinez1985!"
    )

    private fun user() = User(
        id = UUID.randomUUID(),
        firstName = "Albert",
        lastName = "Martinez",
        email = "amartinez@gmail.com",
        passwordHash = "encoded-password",
        role = UserRole.USER,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
