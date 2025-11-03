package com.bootcamp.pawssible_API.usuario.repository;

import com.bootcamp.pawssible_API.usuario.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository <Role, Long> {
    Optional<Role> findByName(String name);

}
