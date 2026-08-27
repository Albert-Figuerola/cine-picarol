package com.albert.cinepicarol.auth.adapter

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.config.JwtProperties
import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.UUID

@Component
class JwtTokenAdapter(
    private val jwtProperties: JwtProperties,
) : TokenPort {

    private val key = Keys.hmacShaKeyFor(
        jwtProperties.secret.toByteArray(StandardCharsets.UTF_8),
    )

    override fun generateToken(user: User): String {
        val now = Date()
        val expiration = Date(now.time + jwtProperties.expiration)

        return Jwts.builder()
            .subject(user.id.toString())
            .claim("role", user.role.name)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }

    override fun getUserId(token: String): UUID {
        return UUID.fromString(parseClaims(token).subject)
    }

    override fun getRole(token: String): UserRole {
        return UserRole.valueOf(
            parseClaims(token).get("role", String::class.java)
        )
    }

    override fun isValid(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch (_: JwtException) {
            false
        } catch (_: IllegalArgumentException) {
            false
        }
    }

    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}
