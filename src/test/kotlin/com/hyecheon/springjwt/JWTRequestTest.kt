package com.hyecheon.springjwt

import com.hyecheon.springjwt.config.UserLoginForm
import com.hyecheon.springjwt.domain.sp_user.SpUser
import com.hyecheon.springjwt.domain.sp_user.SpUserRepository
import com.hyecheon.springjwt.domain.sp_user.SpUserService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.lang.Thread.sleep

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
class JWTRequestTest : WebIntegrationTest() {

    @Autowired
    lateinit var userRepository: SpUserRepository

    @Autowired
    lateinit var userService: SpUserService

    @BeforeEach
    internal fun setUp() {
        userRepository.deleteAll()
        val user = userService.save(SpUser().apply {
            email = "user1"
            enabled = true
            setPassword("1111")
        })
        userService.addAuthority(user.userId!!, "ROLE_USER")
    }

    @DisplayName("1. hello 메시지를 받아 온다")
    @Test
    internal fun test_1() {
        val client = RestTemplate()
        val token = getToken()

        println(token)

        val headers = HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, token.authToken)
        val body = HttpEntity(null, headers)
        val resp2 = client.exchange<String>(uri("/greeting"), HttpMethod.GET, body)

        Assertions.assertThat(resp2.body).isEqualTo("hello")
    }

    @DisplayName("2. 토큰 만료 테스트")
    @Test
    internal fun test_2() {

        val client = RestTemplate()
        val token = getToken()

        sleep(3000)
        val headers = HttpHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer ${token.authToken}")
        val body = HttpEntity(null, headers)
        assertThrows(Exception::class.java, {
            val resp2 = client.exchange<String>(uri("/greeting"), HttpMethod.GET, body)
            Assertions.assertThat(resp2.body).isEqualTo("hello")
        })

        val refreshToken = refreshToken(token.refreshToken!!)
        val headers2 = HttpHeaders()
        headers2.add(HttpHeaders.AUTHORIZATION, "Bearer ${refreshToken.authToken}")
        val body2 = HttpEntity(null, headers2)

        val resp2 = client.exchange<String>(uri("/greeting"), HttpMethod.GET, body2)

        Assertions.assertThat(resp2.body).isEqualTo("hello")
    }

    private fun getToken() = run {
        val client = RestTemplate()
        val body = HttpEntity<UserLoginForm>(
            UserLoginForm("user1", "1111")
        )
        val resp = client.exchange<SpUser>(uri("/login"), HttpMethod.POST, body)
        TokenBox(authToken = resp.headers["auth_token"]?.get(0),
            refreshToken = resp.headers["refresh_token"]?.get(0)
        )
    }

    private fun refreshToken(refreshToken: String) = run {
        val client = RestTemplate()
        val body = HttpEntity<UserLoginForm>(UserLoginForm(refreshToken = refreshToken))

        val resp = client.exchange<SpUser>(uri("/login"), HttpMethod.POST, body)

        TokenBox(authToken = resp.headers["auth_token"]?.get(0),
            refreshToken = resp.headers["refresh_token"]?.get(0)
        )
    }
}