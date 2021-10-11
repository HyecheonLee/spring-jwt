package com.hyecheon.springjwt.domain.sp_user

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

/**
 * User: hyecheon lee
 * Email: rainbow880616@gmail.com
 * Date: 2021/10/11
 */

@Entity
@Table(name = "sp_user_authority")
@IdClass(SpAuthority::class)
class SpAuthority : GrantedAuthority {

    @Id
    @Column(name = "user_id")
    var userId: Long? = null
        private set

    @Id
    @Column(name = "authority")
    private var authority: String? = null

    constructor(userId: Long?, authority: String?) {
        this.userId = userId
        this.authority = authority
    }


    override fun getAuthority(): String? {
        return authority
    }

}
