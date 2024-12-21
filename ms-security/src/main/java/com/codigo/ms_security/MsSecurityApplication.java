package com.codigo.ms_security;

import com.codigo.ms_security.entity.Role;
import com.codigo.ms_security.entity.RoleType;
import com.codigo.ms_security.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
public class MsSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(MsSecurityApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {
		return args -> {
			// Inicializar roles si no existen
			if (!roleRepository.existsByNombreRol(RoleType.USER)) {
				Role userRole = Role.builder()
						.nombreRol(RoleType.USER)
						.build();
				roleRepository.save(userRole);
			}
			if (!roleRepository.existsByNombreRol(RoleType.ADMIN)) {
				Role adminRole = Role.builder()
						.nombreRol(RoleType.ADMIN)
						.build();
				roleRepository.save(adminRole);
			}
		};
	}
}
