package com.albert.cinepicarol.user.controller

import com.albert.cinepicarol.common.response.ApiResponse
import com.albert.cinepicarol.user.command.request.CreateUserRequest
import com.albert.cinepicarol.user.command.usecase.CreateUserUseCase
import com.albert.cinepicarol.user.command.usecase.GetCurrentUserUseCase
import com.albert.cinepicarol.user.mapper.toResponse
import com.albert.cinepicarol.user.query.response.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @Valid @RequestBody request: CreateUserRequest
    ) : ApiResponse<UserResponse> {
        val user = createUserUseCase.execute(request)

        return ApiResponse(
            data = user.toResponse()
        )
    }

    @GetMapping("/me")
    fun getMe(
        authentication: Authentication
    ): ApiResponse<UserResponse> {
        val userId = authentication.principal as UUID
        val user = getCurrentUserUseCase.execute(userId)

        return ApiResponse(
            data = user.toResponse()
        )
    }

}