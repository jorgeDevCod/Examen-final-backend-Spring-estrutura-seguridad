package com.codigo.ms_security.Service;

import com.codigo.ms_security.Aggregates.SignUpRequest;
import com.codigo.ms_security.Aggregates.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserResponse createUser(SignUpRequest request);
    UserResponse getUserByUsername(String username);
    UserDetailsService userDetailsService();
    Boolean existsByEmail(String email);
}