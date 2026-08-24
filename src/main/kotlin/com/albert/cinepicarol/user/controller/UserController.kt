package com.albert.cinepicarol.user.controller

import com.albert.cinepicarol.common.response.ApiResponse
import com.albert.cinepicarol.user.command.request.CreateUserRequest
import com.albert.cinepicarol.user.command.usecase.CreateUserUseCase
import com.albert.cinepicarol.user.mapper.toResponse
import com.albert.cinepicarol.user.query.response.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val createUserUseCase: CreateUserUseCase
) {

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @Valid @RequestBody request: CreateUserRequest
    ) : ApiResponse<UserResponse> {
        val user = createUserUseCase.execute(request)

        return ApiResponse(
            data = user.toResponse()
        )
    }

}