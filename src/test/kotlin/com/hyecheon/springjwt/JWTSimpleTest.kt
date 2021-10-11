package com.hyecheon.springjwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep
import java.util.*
import javax.xml.bind.DatatypeConverter

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */

class JWTSimpleTest {
    private val log = LoggerFactory.getLogger(this::class.java)

    @DisplayName("1. jjwt를 이용한 토큰 테스트")
    @Test
    fun test_1() {
        val oktaToken = Jwts.builder().addClaims(
            mapOf("name" to "hyecheon", "price" to 3000)
        )
            .signWith(SignatureAlgorithm.HS256, "hyecheon")
            .compact()

        log.info(oktaToken)
        printToken(oktaToken)

        val tokenInfo = Jwts.parser().setSigningKey("hyecheon").parseClaimsJws(oktaToken)
        println(tokenInfo)

    }


    @DisplayName("2. java-jwt 를 이용한 토큰 테스트")
    @Test
    fun test_2() {
        val SEC_KEY = DatatypeConverter.parseBase64Binary("hyecheon")

        val oauthToken = JWT.create().withClaim("name", "hyecheon")
            .withClaim("price", 3000)
            .sign(Algorithm.HMAC256(SEC_KEY))
        log.info(oauthToken)
        printToken(oauthToken)

        val verify = JWT.require(Algorithm.HMAC256(SEC_KEY)).build().verify(oauthToken)
        println(verify.claims)

        val tokenInfo = Jwts.parser().setSigningKey(SEC_KEY).parseClaimsJws(oauthToken)
        println(tokenInfo)
    }


    @DisplayName("3. 만료 시간 테스트")
    @Test
    internal fun test_3() {
        val AL = Algorithm.HMAC256("hyecheon")
        val token = JWT.create()
            .withSubject("a1234")
            .withNotBefore(Date(System.currentTimeMillis() + 1000))
            .withExpiresAt(Date(System.currentTimeMillis() + 3000))
            .sign(AL)

        try {
            val verify = JWT.require(AL).build().verify(token)
            println(verify.claims)
        } catch (e: Exception) {
            println("유효하지 않은 토큰입니다.")
            val decode = JWT.decode(token)
            println(decode.claims)
        }
    }


    private fun printToken(token: String) = run {
        val tokens = token.split(".")
        log.info("header: {}", String(Base64.getDecoder().decode(tokens[0])))
        log.info("body: {}", String(Base64.getDecoder().decode(tokens[1])))
    }

}