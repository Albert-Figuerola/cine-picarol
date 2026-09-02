package com.albert.cinepicarol.user.usecase

import com.albert.cinepicarol.user.command.model.UpdateUserCommand
import com.albert.cinepicarol.user.command.usecase.UpdateCurrentUserUseCase
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import com.albert.cinepicarol.user.exception.UserNotFoundException
import com.albert.cinepicarol.user.port.UserPort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertNotEquals

class UpdateCurrentUserUseCaseTest {

    private val userPort = mock<UserPort>()
    private val updateCurrentUserUseCase = UpdateCurrentUserUseCase(userPort)

    @Test
    fun `should update first name and keep last name`() {
        val user = createUser()

        val command = UpdateUserCommand(
            firstName = "Sergi",
            lastName = null
        )

        whenever(userPort.findById(user.id))
            .thenReturn(user)

        whenever(userPort.save(any<User>()))
            .thenAnswer { it.arguments[0] as User }

        val result = updateCurrentUserUseCase.execute(
            user.id,
            command
        )

        assertEquals("Sergi", result.firstName)
        assertEquals(user.lastName, result.lastName)
        assertNotEquals(user.updatedAt, result.updatedAt)

        verify(userPort).findById(user.id)
        verify(userPort).save(any<User>())
    }

    @Test
    fun `should update last name and keep first name`() {
        val user = createUser()
        val command = UpdateUserCommand(
            firstName = null,
            lastName = "Garcia"
        )

        whenever(userPort.findById(user.id))
            .thenReturn(user)
        whenever(userPort.save(any<User>()))
            .thenAnswer { it.arguments[0] as User }

        val result = updateCurrentUserUseCase.execute(user.id, command)

        assertEquals(user.firstName, result.firstName)
        assertEquals("Garcia", result.lastName)
        assertNotEquals(user.updatedAt, result.updatedAt)

        verify(userPort).findById(user.id)
        verify(userPort).save(any<User>())
    }

    @Test
    fun `should update first name and last name`() {
        val user = createUser()
        val command = UpdateUserCommand(
            firstName = "Sergi",
            lastName = "Garcia"
        )

        whenever(userPort.findById(user.id))
            .thenReturn(user)
        whenever(userPort.save(any<User>()))
            .thenAnswer { it.arguments[0] as User }

        val result = updateCurrentUserUseCase.execute(user.id, command)

        assertEquals("Sergi", result.firstName)
        assertEquals("Garcia", result.lastName)
        assertNotEquals(user.updatedAt, result.updatedAt)

        verify(userPort).findById(user.id)
        verify(userPort).save(any<User>())
    }

    @Test
    fun `should throw exception when user does not exist`() {
        val userId = UUID.randomUUID()
        val command = UpdateUserCommand(
            firstName = "Sergi",
            lastName = "Garcia"
        )

        whenever(userPort.findById(userId))
            .thenReturn(null)

        assertThrows<UserNotFoundException> {
            updateCurrentUserUseCase.execute(userId, command)
        }

        verify(userPort).findById(userId)
        verify(userPort, never()).save(any<User>())
    }

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
