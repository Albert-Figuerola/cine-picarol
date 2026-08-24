package com.albert.cinepicarol.user.command.usecase

import com.albert.cinepicarol.user.command.request.CreateUserRequest
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import com.albert.cinepicarol.user.exception.UserAlreadyExistsException
import com.albert.cinepicarol.user.port.PasswordEncoderPort
import com.albert.cinepicarol.user.port.UserPort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreateUserUseCase (
    private val userPort: UserPort,
    private val passwordEncoderPort: PasswordEncoderPort
) {

    fun execute(request: CreateUserRequest): User {
        val existingUser = userPort.findByEmail(request.email)

        if (existingUser != null) {
            throw UserAlreadyExistsException(request.email)
        }

        val passwordHash = passwordEncoderPort.encode(request.password)

        val user = User(
            id = UUID.randomUUID(),
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            passwordHash = passwordHash,
            role = UserRole.USER,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return userPort.save(user)
    }

}