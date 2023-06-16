package com.quyvx.accommodationbooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "account")
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    private String address;

    private String phone;

    private int score;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Hotel> hotels;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    public Account(){
        this.score = 100;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
