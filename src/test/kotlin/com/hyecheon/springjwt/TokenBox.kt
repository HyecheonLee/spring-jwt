package com.hyecheon.springjwt

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
data class TokenBox(
    val authToken: String?,
    val refreshToken: String?,
)