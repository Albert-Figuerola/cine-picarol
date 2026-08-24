package com.albert.cinepicarol.user.adapter

import com.albert.cinepicarol.user.port.PasswordEncoderPort
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BCryptPasswordEncoderAdapter : PasswordEncoderPort {

    private val passwordEncoder = BCryptPasswordEncoder()

    override fun encode(rawPassword: String): String {
        return passwordEncoder.encode(rawPassword)
    }

}