package com.albert.cinepicarol.user.adapter

import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.mapper.toDomain
import com.albert.cinepicarol.user.mapper.toEntity
import com.albert.cinepicarol.user.port.UserPort
import com.albert.cinepicarol.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserJpaAdapter(
    private val userRepository: UserRepository
) : UserPort {

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)?.toDomain()
    }

    override fun save(user: User): User {
        return userRepository.save(user.toEntity()).toDomain()
    }

    override fun findById(id: UUID): User? {
        return userRepository.findByIdOrNull(id)?.toDomain()
    }

}