package com.bootcamp.pawssible_API.usuario.repository;

import com.bootcamp.pawssible_API.usuario.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>  {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
