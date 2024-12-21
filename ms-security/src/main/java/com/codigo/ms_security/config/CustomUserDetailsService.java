package com.codigo.ms_security.config;

import com.codigo.ms_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        return userRepository.findByEmail(emailOrUsername)
                .orElseGet(() -> userRepository.findByUsername(emailOrUsername)
                        .orElseThrow(() -> {
                            log.error("Usuario no encontrado con email/username: {}", emailOrUsername);
                            return new UsernameNotFoundException("Usuario no encontrado: " + emailOrUsername);
                        }));
    }
}