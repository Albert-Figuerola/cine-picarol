package com.albert.cinepicarol.user

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.user.command.request.CreateUserRequest
import com.albert.cinepicarol.user.command.usecase.CreateUserUseCase
import com.albert.cinepicarol.user.command.usecase.GetCurrentUserUseCase
import com.albert.cinepicarol.user.controller.UserController
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import com.albert.cinepicarol.user.exception.UserAlreadyExistsException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var createUserUseCase: CreateUserUseCase

    @MockitoBean
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    fun `should return 201 when user is created`() {
        val user = createUser()

        whenever(createUserUseCase.execute(any<CreateUserRequest>()))
            .thenReturn(user)

        mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(createUserRequest())
                )
        )
            .andExpect(status().isCreated)

            .andExpect(jsonPath("$.data.firstName").value(user.firstName))
            .andExpect(jsonPath("$.data.lastName").value(user.lastName))
            .andExpect(jsonPath("$.data.email").value(user.email))
            .andExpect(jsonPath("$.data.role").value("USER"))

        verify(createUserUseCase).execute(any<CreateUserRequest>())
    }

    @Test
    fun `should return 409 when email already exists`() {
        val request = createUserRequest()

        doThrow(UserAlreadyExistsException(request.email))
            .whenever(createUserUseCase)
            .execute(any<CreateUserRequest>())

        mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("USER_ALREADY_EXISTS"))
            .andExpect(jsonPath("$.message").value("User ${request.email} already exists"))

        verify(createUserUseCase).execute(any<CreateUserRequest>())
    }

    @Test
    fun `should return 400 when first name is blank`() {
        val request = createUserRequest()

        mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(request.copy(firstName = ""))
                )
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Firstname cannot be empty"))

        verifyNoInteractions(createUserUseCase)
    }

    @Test
    fun `should return 400 when last name is blank`() {
        val request = createUserRequest().copy(lastName = "")

        mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Lastname cannot be empty"))

        verifyNoInteractions(createUserUseCase)
    }

    @Test
    fun `should return 400 when email is invalid`() {
        val request = createUserRequest().copy(email = "invalid-email")

        mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("User email must be valid"))

        verifyNoInteractions(createUserUseCase)
    }

    @Test
    fun `should return 400 when password is blank`() {
        val request = createUserRequest().copy(password = "")

        mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(jsonPath("$.message").value("Password cannot be empty"))

        verifyNoInteractions(createUserUseCase)
    }

    @Test
    fun `should return 200 when getting current user`() {
        val user = createUser()
        val authentication = UsernamePasswordAuthenticationToken(
            user.id,
            null,
            emptyList()
        )

        whenever(getCurrentUserUseCase.execute(user.id))
            .thenReturn(user)

        mockMvc.perform(
            get("/api/v1/users/me")
                .principal(authentication)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.id").value(user.id.toString()))
            .andExpect(jsonPath("$.data.email").value(user.email))

        verify(getCurrentUserUseCase).execute(user.id)
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
