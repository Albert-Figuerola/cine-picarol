package com.albert.cinepicarol.auth.adapter

import com.albert.cinepicarol.config.JwtProperties
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JwtTokenAdapterTest {

    private val jwtProperties = JwtProperties(
        secret = "a-secure-test-secret-that-is-at-least-32-bytes",
        expiration = 3_600_000,
    )
    private val jwtTokenAdapter = JwtTokenAdapter(jwtProperties)

    @Test
    fun `should generate a valid token`() {
        val token = jwtTokenAdapter.generateToken(createUser())

        assertTrue(jwtTokenAdapter.isValid(token))
    }

    @Test
    fun `should extract user id from token`() {
        val user = createUser()
        val token = jwtTokenAdapter.generateToken(user)

        assertEquals(user.id, jwtTokenAdapter.getUserId(token))
    }

    @Test
    fun `should extract role from token`() {
        val user = createUser(role = UserRole.ADMIN)
        val token = jwtTokenAdapter.generateToken(user)

        assertEquals(UserRole.ADMIN, jwtTokenAdapter.getRole(token))
    }

    @Test
    fun `should return false when token is expired`() {
        val adapter = JwtTokenAdapter(jwtProperties.copy(expiration = -1))
        val token = adapter.generateToken(createUser())

        assertFalse(adapter.isValid(token))
    }

    @Test
    fun `should return false when token signature is invalid`() {
        val token = jwtTokenAdapter.generateToken(createUser())
        val adapterWithDifferentSecret = JwtTokenAdapter(
            jwtProperties.copy(secret = "a-different-test-secret-that-is-32-bytes-long"),
        )

        assertFalse(adapterWithDifferentSecret.isValid(token))
    }

    @Test
    fun `should return false when token is malformed`() {
        assertFalse(jwtTokenAdapter.isValid("not-a-jwt"))
    }

    @Test
    fun `should return false when token is empty`() {
        assertFalse(jwtTokenAdapter.isValid(""))
    }

    private fun createUser(role: UserRole = UserRole.USER) = User(
        id = UUID.randomUUID(),
        firstName = "Albert",
        lastName = "Martinez",
        email = "amartinez@gmail.com",
        passwordHash = "encoded-password",
        role = role,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )

}
