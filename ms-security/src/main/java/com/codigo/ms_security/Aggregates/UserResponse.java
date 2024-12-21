package com.codigo.ms_security.Aggregates;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponse {
    private String username;
    private String email;
    private String password; // Mostrar mensaje personalizado
    private String nombres;
    private String apellidos;
    private String tipoDoc;
    private String numDoc;
    private Set<String> roles;
}
