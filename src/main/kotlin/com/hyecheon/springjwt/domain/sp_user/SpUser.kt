package com.hyecheon.springjwt.domain.sp_user

import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*


@Entity
@Table(name = "sp_user")
class SpUser : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Long? = null

    var email: String? = null

    private var password: String? = null

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "user_id"))
    private var authorities: Set<SpAuthority> = mutableSetOf()

    var enabled = false

    override fun getAuthorities(): Set<SpAuthority> {
        return authorities
    }


    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String {
        return email!!
    }

    override fun isAccountNonExpired(): Boolean {
        return enabled
    }

    override fun isAccountNonLocked(): Boolean {
        return enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return enabled
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    fun setAuthorities(authorities: Set<SpAuthority>) {
        this.authorities = authorities
    }

    fun setPassword(password: String) {
        this.password = password
    }

    override fun toString(): String {
        return "SpUser(userId=$userId, email=$email, password=$password, authorities=$authorities, enabled=$enabled)"
    }
}