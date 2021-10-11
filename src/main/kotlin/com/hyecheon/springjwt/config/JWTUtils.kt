package com.hyecheon.springjwt.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.hyecheon.springjwt.domain.sp_user.SpUser
import org.slf4j.LoggerFactory

import java.time.Instant

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
class JWTUtils {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
        private val algorithm: Algorithm = Algorithm.HMAC256("hyecheon")

        //        private const val AuthTime = 20 * 60
        private const val AuthTime = 2
        private const val RefreshTime = 60 * 60 * 24 * 7
        fun makeAuthToken(user: SpUser) = run {
            JWT.create()
                .withSubject(user.username)
                .withClaim("exp", Instant.now().epochSecond + AuthTime)
                .sign(algorithm)
        }

        fun makeRefreshToken(user: SpUser) = run {
            JWT.create()
                .withSubject(user.username)
                .withClaim("exp", Instant.now().epochSecond + RefreshTime)
                .sign(algorithm)
        }

        fun verify(token: String) = run {
            try {
                val verify = JWT.require(algorithm).build().verify(token)
                VerifyResult(verify.subject, success = true)
            } catch (e: TokenExpiredException) {
                val decode = JWT.decode(token)
                VerifyResult(decode.subject, false)
            } catch (e: Exception) {
                log.error("verify {}", e.message, e)
                val decode = JWT.decode(token)
                VerifyResult(decode.subject, false)
            }
        }
    }
}