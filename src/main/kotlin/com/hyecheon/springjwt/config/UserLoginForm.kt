package com.hyecheon.springjwt.config

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */
data class UserLoginForm(
    val username: String? = null,
    val password: String? = null,
    val refreshToken: String? = null,

    )