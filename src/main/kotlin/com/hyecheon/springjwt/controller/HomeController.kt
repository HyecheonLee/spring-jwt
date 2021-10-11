package com.hyecheon.springjwt.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/10
 */
@RestController
class HomeController {
    @GetMapping
    fun index() = run {
        "hello world"
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/greeting")
    fun greeting() = run {
        "hello"
    }
}