package com.albert.cinepicarol.auth.controller

import com.albert.cinepicarol.auth.command.request.LoginRequest
import com.albert.cinepicarol.auth.command.usecase.LoginUseCase
import com.albert.cinepicarol.auth.mapper.toResponse
import com.albert.cinepicarol.auth.response.LoginResponse
import com.albert.cinepicarol.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController (
    private val loginUseCase: LoginUseCase
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ) : ApiResponse<LoginResponse> {
        val result = loginUseCase.execute(request)

        return ApiResponse(result.toResponse())
    }

}