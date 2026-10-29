package com.spendwise.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.spendwise.config.Role;

@Setter
@Entity
@Table(name = "users")
@Getter 
@Builder
public class User implements UserDetails {

    // public User(String username, String email, String passwordHash, BigDecimal monthlyIncome) {
	// 	this.username=username;
    //     this.email=email;
    //     this.passwordHash=passwordHash;
    //     this.monthlyIncome=monthlyIncome;
	// }
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String passwordHash;
    @Column(precision = 12, scale = 2)
    private BigDecimal monthlyIncome;
    @ManyToOne
    @JoinColumn(name = "currency_id",nullable = false)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private Role role;
    //The method “getAuthorities()” returns the user’s roles list; it is helpful to manage permissions. We return an empty list because we will not cover role-based access control.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
