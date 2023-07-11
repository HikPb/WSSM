package com.project.wsms.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.wsms.model.Employee;

public class CustomUserDetails implements UserDetails {

    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String fullname;
    private String phone;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Integer id, String username, String password, String fullname, String phone,
        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.phone = phone;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserDetails build(Employee user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
            .collect(Collectors.toList());

        return new CustomUserDetails(
            user.getId(), 
            user.getUsername(), 
            user.getPassword(), 
            user.getFullname(),
            user.getPhone(),
            authorities);
    }
    
    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullname() {
        return fullname;
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