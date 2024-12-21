package com.codigo.ms_security.Service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Set;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails, Set<String> authorities);
    Map<String, Object> extractAllClaims(String token);
    boolean validateToken(String token, UserDetails userDetails);
}

