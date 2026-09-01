package com.albert.cinepicarol.user.command.usecase

import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.exception.UserNotFoundException
import com.albert.cinepicarol.user.port.UserPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetCurrentUserUseCase (
    private val userPort: UserPort
) {

    fun execute(userId: UUID) : User {
        return userPort.findById(userId)
            ?: throw UserNotFoundException(userId)
    }

}