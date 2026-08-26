package com.albert.cinepicarol.auth

import com.albert.cinepicarol.auth.command.request.LoginRequest
import com.albert.cinepicarol.auth.command.usecase.LoginUseCase
import com.albert.cinepicarol.auth.exception.InvalidCredentialsException
import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import com.albert.cinepicarol.user.port.PasswordEncoderPort
import com.albert.cinepicarol.user.port.UserPort
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

class LoginUseCaseTest {

    private val userPort = mock<UserPort>()
    private val passwordEncoderPort = mock<PasswordEncoderPort>()
    private val tokenPort = mock<TokenPort>()
    private val loginUseCase = LoginUseCase(userPort, passwordEncoderPort, tokenPort)

    @Test
    fun `should return login result when credentials are correct`() {
        val request = loginRequest()
        val user = createUser()
        val token = "generated-token"

        whenever(userPort.findByEmail(request.email))
            .thenReturn(user)
        whenever(passwordEncoderPort.matches(request.password, user.passwordHash))
            .thenReturn(true)
        whenever(tokenPort.generateToken(user))
            .thenReturn(token)

        val result = loginUseCase.execute(request)

        assertEquals(user, result.user)
        assertEquals(token, result.token)
        verify(userPort).findByEmail(request.email)
        verify(passwordEncoderPort).matches(request.password, user.passwordHash)
        verify(tokenPort).generateToken(user)
    }

    @Test
    fun `should throw invalid credentials when email does not exist`() {
        val request = loginRequest()

        whenever(userPort.findByEmail(request.email))
            .thenReturn(null)

        assertThrows<InvalidCredentialsException> {
            loginUseCase.execute(request)
        }

        verify(userPort).findByEmail(request.email)
        verifyNoInteractions(passwordEncoderPort)
        verifyNoInteractions(tokenPort)
    }

    @Test
    fun `should throw invalid credentials when password is wrong`() {
        val request = loginRequest()
        val user = createUser()

        whenever(userPort.findByEmail(request.email))
            .thenReturn(user)
        whenever(passwordEncoderPort.matches(request.password, user.passwordHash))
            .thenReturn(false)

        assertThrows<InvalidCredentialsException> {
            loginUseCase.execute(request)
        }

        verify(userPort).findByEmail(request.email)
        verify(passwordEncoderPort).matches(request.password, user.passwordHash)
        verifyNoInteractions(tokenPort)
    }

    private fun loginRequest() = LoginRequest(
        email = "amartinez@gmail.com",
        password = "M@rtinez1985!"
    )

    private fun createUser() = User(
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
