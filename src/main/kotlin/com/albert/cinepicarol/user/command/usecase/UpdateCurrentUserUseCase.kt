package com.albert.cinepicarol.user.command.usecase

import com.albert.cinepicarol.user.command.model.UpdateUserCommand
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.exception.UserNotFoundException
import com.albert.cinepicarol.user.port.UserPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
@Transactional
class UpdateCurrentUserUseCase (
    private val userPort: UserPort
) {

    fun execute(userId: UUID, command: UpdateUserCommand): User {
        val user = userPort.findById(userId) ?: throw UserNotFoundException(userId)

        val updatedUser = user.copy(
            firstName = command.firstName ?: user.firstName,
            lastName = command.lastName ?: user.lastName,
            updatedAt = LocalDateTime.now()
        )

        val userSaved = userPort.save(updatedUser)

        return userSaved
    }

}