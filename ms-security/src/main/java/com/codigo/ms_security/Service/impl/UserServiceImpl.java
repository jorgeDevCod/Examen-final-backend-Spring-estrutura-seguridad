package com.codigo.ms_security.Service.impl;

import com.codigo.ms_security.Service.UserService;
import com.codigo.ms_security.Aggregates.SignUpRequest;
import com.codigo.ms_security.Aggregates.UserResponse;
import com.codigo.ms_security.entity.Role;
import com.codigo.ms_security.entity.RoleType;
import com.codigo.ms_security.entity.User;
import com.codigo.ms_security.repository.RoleRepository;
import com.codigo.ms_security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(SignUpRequest request) {

        // Validaciones existentes...
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está registrado");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (userRepository.existsByNumDoc(request.getNumDoc())) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .tipoDoc(request.getTipoDoc())
                .numDoc(request.getNumDoc())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        Set<Role> roles = new HashSet<>();
        RoleType roleType = request.getRoles().contains("ADMIN") ? RoleType.ADMIN : RoleType.USER;
        Role role = roleRepository.findByNombreRol(roleType)
                .orElseThrow(() -> new RuntimeException("Error: Role no encontrado"));
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToUserResponse(user);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .nombres(user.getNombres())
                .apellidos(user.getApellidos())
                .tipoDoc("Privado")
                .numDoc("Privado")
                .password("********")  // Ocultamos la contraseña por seguridad
                .roles(user.getRoles().stream()
                        .map(role -> role.getNombreRol().name())
                        .collect(Collectors.toSet()))
                .build();
    }
}
