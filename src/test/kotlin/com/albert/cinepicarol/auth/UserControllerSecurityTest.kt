package com.albert.cinepicarol.auth

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.auth.security.JwtAuthenticationFilter
import com.albert.cinepicarol.config.SecurityConfig
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import com.albert.cinepicarol.user.command.usecase.CreateUserUseCase
import com.albert.cinepicarol.user.command.usecase.GetCurrentUserUseCase
import com.albert.cinepicarol.user.command.usecase.UpdateCurrentUserUseCase
import com.albert.cinepicarol.user.controller.UserController
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(UserController::class)
@Import(
    SecurityConfig::class,
    JwtAuthenticationFilter::class
)
class UserControllerSecurityTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createUserUseCase: CreateUserUseCase

    @MockitoBean
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @MockitoBean
    private lateinit var updateCurrentUserUseCase: UpdateCurrentUserUseCase

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    @WithAnonymousUser
    fun `should return 401 when updating current user without authentication`() {
        mockMvc.perform(
            patch("/api/v1/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "firstName": "Sergi"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isUnauthorized)

        verifyNoInteractions(updateCurrentUserUseCase)
    }

    @Test
    fun `should return 200 when USER updates current user`() {
        val user = createUser()

        val auth = UsernamePasswordAuthenticationToken(
            user.id,
            null,
            listOf(SimpleGrantedAuthority("ROLE_USER"))
        )

        whenever(updateCurrentUserUseCase.execute(any(), any()))
            .thenReturn(user.copy(firstName = "Sergi"))

        mockMvc.perform(
            patch("/api/v1/users/me")
                .with(authentication(auth))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                    "firstName": "Sergi"
                }
                """.trimIndent()
                )
        )
            .andExpect(status().isOk)
    }

    private fun createUser() = User(
        id = UUID.randomUUID(),
        firstName = "Sergi",
        lastName = "Martinez",
        email = "amartinez@gmail.com",
        passwordHash = "encoded-password",
        role = UserRole.USER,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

}