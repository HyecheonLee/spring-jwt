package com.hyecheon.springjwt.config

import com.hyecheon.springjwt.domain.sp_user.SpUserService
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.naming.AuthenticationException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
class JwtCheckFilter(
    authenticationManager: AuthenticationManager,
    private val userService: SpUserService,
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val bearer = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            chain.doFilter(request, response)
            return
        }
        val token = bearer.substringAfter("Bearer ")
        val result = JWTUtils.verify(token)
        if (result.success) {
            val user = userService.loadUserByUsername(result.username)
            UsernamePasswordAuthenticationToken(user.username, null, user.authorities).apply {
                SecurityContextHolder.getContext().authentication = this
            }
        } else {
            throw AuthenticationException("Token is not valid")
        }
        chain.doFilter(request, response)
    }
}