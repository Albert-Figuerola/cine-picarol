package com.albert.cinepicarol.user.usecase

import com.albert.cinepicarol.user.command.usecase.GetCurrentUserUseCase
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import com.albert.cinepicarol.user.exception.UserNotFoundException
import com.albert.cinepicarol.user.port.UserPort
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID

class GetCurrentUserUseCaseTest {

    private val userPort = mock<UserPort>()
    private val getCurrentUserUseCase = GetCurrentUserUseCase(userPort)

    @Test
    fun `should return current user`() {
        val user = createUser()

        whenever(userPort.findById(user.id))
            .thenReturn(user)

        val result = getCurrentUserUseCase.execute(user.id)

        assertEquals(user, result)
        verify(userPort).findById(user.id)
    }

    @Test
    fun `should throw exception when current user does not exist`() {
        val userId = UUID.randomUUID()

        whenever(userPort.findById(userId))
            .thenReturn(null)

        assertThrows<UserNotFoundException> {
            getCurrentUserUseCase.execute(userId)
        }

        verify(userPort).findById(userId)
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