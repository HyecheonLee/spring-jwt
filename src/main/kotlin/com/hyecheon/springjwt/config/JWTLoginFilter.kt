package com.hyecheon.springjwt.config

import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hyecheon.springjwt.domain.sp_user.SpUser
import com.hyecheon.springjwt.domain.sp_user.SpUserService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
class JWTLoginFilter(
    authenticationManager: AuthenticationManager,
    private val userService: SpUserService,
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    private val objectMapper = ObjectMapper()


    init {
        setFilterProcessesUrl("/login")
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val userLogin = objectMapper.readValue<UserLoginForm>(request.inputStream)
        if (userLogin.refreshToken == null) {

            val token = UsernamePasswordAuthenticationToken(
                userLogin.username, userLogin.password, null
            )
            return authenticationManager.authenticate(token)
        } else {
            val verify = JWTUtils.verify(userLogin.refreshToken)
            if (verify.success) {
                val user = userService.loadUserByUsername(verify.username)
                return UsernamePasswordAuthenticationToken(user, null, user.authorities)
            } else {
                throw TokenExpiredException("refresh token expired")
            }
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication,
    ) {
        (authResult.principal as? SpUser)?.let {
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer ${JWTUtils.makeAuthToken(it)}")
            response.setHeader("auth_token", "${JWTUtils.makeAuthToken(it)}")
            response.setHeader("refresh_token", "${JWTUtils.makeRefreshToken(it)}")
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            response.outputStream.write(objectMapper.writeValueAsBytes(it))
        } ?: super.successfulAuthentication(request, response, chain, authResult)
    }
}