package com.albert.cinepicarol.auth.command.usecase

import com.albert.cinepicarol.auth.command.model.LoginCommand
import com.albert.cinepicarol.auth.domain.LoginResult
import com.albert.cinepicarol.auth.exception.InvalidCredentialsException
import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.user.port.PasswordEncoderPort
import com.albert.cinepicarol.user.port.UserPort
import org.springframework.stereotype.Service

@Service
class LoginUseCase (
    private val userPort: UserPort,
    private val passwordEncoderPort: PasswordEncoderPort,
    private val tokenPort: TokenPort
) {

    fun execute(request: LoginCommand) : LoginResult {
        val user = userPort.findByEmail(request.email)
            ?: throw InvalidCredentialsException()

        val passwordMatches = passwordEncoderPort.matches(
            request.password,
            user.passwordHash
        )

        if (!passwordMatches) {
            throw InvalidCredentialsException()
        }

        val token = tokenPort.generateToken(user)

        return LoginResult(
            user = user,
            token = token
        )
    }

}