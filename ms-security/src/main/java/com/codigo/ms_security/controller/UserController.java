package com.codigo.ms_security.controller;

import com.codigo.ms_security.Service.JwtService;
import com.codigo.ms_security.Service.UserService;
import com.codigo.ms_security.Aggregates.SignUpRequest;
import com.codigo.ms_security.Aggregates.UserResponse;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody SignUpRequest request) {
        try {
            request.setRoles(Set.of("USER"));
            return ResponseEntity.ok(userService.createUser(request));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage());
        }
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        try {
            UserResponse user = userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage());
        }
    }
}