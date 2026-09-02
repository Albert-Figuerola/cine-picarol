package com.albert.cinepicarol.user.usecase

import com.albert.cinepicarol.user.command.request.CreateUserRequest
import com.albert.cinepicarol.user.command.usecase.CreateUserUseCase
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import com.albert.cinepicarol.user.exception.UserAlreadyExistsException
import com.albert.cinepicarol.user.port.PasswordEncoderPort
import com.albert.cinepicarol.user.port.UserPort
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.never
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

class CreateUserUseCaseTest {

    private val userPort = mock<UserPort>()
    private val passwordEncoderPort = mock<PasswordEncoderPort>()
    private val createUserUseCase = CreateUserUseCase(userPort, passwordEncoderPort)

    @Test
    fun `should create user`() {
        val request = createUserRequest()
        val encodedPassword = "encoded-password"

        whenever(userPort.findByEmail(request.email))
            .thenReturn(null)

        whenever(passwordEncoderPort.encode(request.password))
            .thenReturn(encodedPassword)

        whenever(userPort.save(any<User>()))
            .thenAnswer { it.arguments[0] as User }

        val result = createUserUseCase.execute(request)

        assertEquals(request.firstName, result.firstName)
        assertEquals(request.lastName, result.lastName)
        assertEquals(request.email, result.email)
        assertEquals(encodedPassword, result.passwordHash)

        verify(userPort).findByEmail(request.email)
        verify(passwordEncoderPort).encode(request.password)
        verify(userPort).save(any<User>())
    }

    @Test
    fun `should throw exception when email already exists`() {
        val request = createUserRequest()
        val user = createUser()

        whenever(userPort.findByEmail(request.email))
            .thenReturn(user)

        assertThrows<UserAlreadyExistsException> {
            createUserUseCase.execute(request)
        }

        verify(userPort).findByEmail(request.email)
        verifyNoInteractions(passwordEncoderPort)
        verify(userPort, never()).save(any<User>())
    }

    private fun createUserRequest() = CreateUserRequest(
        firstName = "Albert",
        lastName = "Martinez",
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