package com.hyecheon.springjwt.domain.sp_user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
interface SpUserRepository : JpaRepository<SpUser, Long> {
    fun findUserByEmail(email: String): Optional<SpUser>
}