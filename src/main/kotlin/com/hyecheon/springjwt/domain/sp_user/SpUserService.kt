package com.hyecheon.springjwt.domain.sp_user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import javax.transaction.Transactional

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */

@Service
@Transactional
class SpUserService : UserDetailsService {
    @Autowired
    lateinit var userRepository: SpUserRepository


    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        return userRepository.findUserByEmail(username!!).orElseThrow { UsernameNotFoundException(username) }
    }

    fun findUser(email: String?): Optional<SpUser> {
        return userRepository.findUserByEmail(email!!)
    }

    fun save(user: SpUser): SpUser {
        return userRepository.save(user)
    }

    fun addAuthority(userId: Long, authority: String?) {
        userRepository.findById(userId).ifPresent { user ->
            val newRole = SpAuthority(user.userId, authority)
            if (!user.authorities.contains(newRole)) {
                val authorities = HashSet<SpAuthority>()
                authorities.addAll(user.authorities)
                authorities.add(newRole)
                user.authorities = authorities
                save(user)
            }
        }
    }

    fun removeAuthority(userId: Long, authority: String?) {
        userRepository.findById(userId).ifPresent { user ->
            val targetRole = SpAuthority(user.userId, authority)
            if (user.authorities.contains(targetRole)) {
                user.authorities = user.authorities.stream().filter { auth -> !auth.equals(targetRole) }
                    .collect(Collectors.toSet())
                save(user)
            }
        }
    }
}
