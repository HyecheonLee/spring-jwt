package com.hyecheon.springjwt.config

import com.hyecheon.springjwt.domain.sp_user.SpUserService
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val userService: SpUserService,
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder = run {
        NoOpPasswordEncoder.getInstance()
    }

    override fun configure(http: HttpSecurity) {
        val loginFilter = JWTLoginFilter(authenticationManager(), userService)
        val checkFilter = JwtCheckFilter(authenticationManager(), userService)


        http.csrf()
            .disable()
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAt(checkFilter, BasicAuthenticationFilter::class.java)
    }
}