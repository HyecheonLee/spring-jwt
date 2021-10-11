package com.hyecheon.springjwt

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import java.lang.IllegalArgumentException
import java.net.URI
import javax.persistence.criteria.CriteriaBuilder

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebIntegrationTest {
    @LocalServerPort
    lateinit var port: String

    fun uri(path: String) = run {
        try {
            URI("http://localhost:${port}${path}")
        } catch (e: Exception) {
            throw IllegalArgumentException(e.message)
        }
    }
}