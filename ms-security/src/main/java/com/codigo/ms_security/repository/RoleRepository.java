package com.codigo.ms_security.repository;

import com.codigo.ms_security.entity.Role;
import com.codigo.ms_security.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombreRol(RoleType nombreRol);
    boolean existsByNombreRol(RoleType nombreRol);
}
