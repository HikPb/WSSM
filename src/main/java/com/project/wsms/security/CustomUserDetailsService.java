package com.project.wsms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.wsms.model.Employee;
import com.project.wsms.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository repository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
        return new CustomUserDetails(user);

    }
}