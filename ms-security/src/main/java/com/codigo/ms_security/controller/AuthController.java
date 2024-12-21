package com.codigo.ms_security.controller;

import com.codigo.ms_security.Aggregates.AuthResponse;
import com.codigo.ms_security.Aggregates.LoginRequest;
import com.codigo.ms_security.Aggregates.SignUpRequest;
import com.codigo.ms_security.Aggregates.UserResponse;
import com.codigo.ms_security.Service.JwtService;
import com.codigo.ms_security.Service.UserService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/signup/admin")
    public ResponseEntity<UserResponse> signUpAdmin(@Valid @RequestBody SignUpRequest request) {
        request.setRoles(Set.of("ADMIN"));
        return ResponseEntity.ok(userService.createUser(request));
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest request) {
        try {
            // Autenticación del usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails user = userService.userDetailsService()
                    .loadUserByUsername(request.getEmail());
            Set<String> authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            String token = jwtService.generateToken(user, authorities);
            Map<String, Object> claims = jwtService.extractAllClaims(token);
            System.out.println("Token claims: " + claims);

            return ResponseEntity.ok(AuthResponse.builder()
                    .token(token)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("Error en la autenticación: " + e.getMessage());
        }
    }

    @GetMapping("/generate-key")
    public ResponseEntity<String> generateKey() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        return ResponseEntity.ok(secretKey);
    }
}