package com.bootcamp.pawssible_API.usuario.service;


import com.bootcamp.pawssible_API.usuario.dto.UserCreateRequest;
import com.bootcamp.pawssible_API.usuario.dto.UserResponse;
import com.bootcamp.pawssible_API.usuario.dto.UserUpdateRequest;
import com.bootcamp.pawssible_API.usuario.exception.BadRequestException;
import com.bootcamp.pawssible_API.usuario.exception.NotFoundException;
import com.bootcamp.pawssible_API.usuario.model.Role;
import com.bootcamp.pawssible_API.usuario.model.User;
import com.bootcamp.pawssible_API.usuario.repository.RoleRepository;
import com.bootcamp.pawssible_API.usuario.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    public List<UserResponse> list(){
        return userRepo.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse get(Long id){
        User u = userRepo.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return toResponse(u);
    }

    @Transactional
    public UserResponse create(UserCreateRequest req){
        if (userRepo.existsByEmail(req.email())) {
            throw new BadRequestException("El email ya está registrado");
        }
        Role role = roleRepo.findById(req.roleId())
                .orElseThrow(() -> new BadRequestException("Rol inválido"));

        User u = User.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .password(encoder.encode(req.password()))
                .enabled(req.enabled() == null ? true : req.enabled())
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        userRepo.save(u);
        return toResponse(u);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest req){
        User u = userRepo.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Si cambia el email, verificar duplicado
        if (!u.getEmail().equals(req.email()) && userRepo.existsByEmail(req.email())) {
            throw new BadRequestException("El email ya está registrado");
        }
        Role role = roleRepo.findById(req.roleId())
                .orElseThrow(() -> new BadRequestException("Rol inválido"));

        u.setFirstName(req.firstName());
        u.setLastName(req.lastName());
        u.setEmail(req.email());
        if (req.password() != null && !req.password().isBlank()) {
            u.setPassword(encoder.encode(req.password()));
        }
        u.setEnabled(req.enabled());
        u.setRole(role);
        u.setUpdatedAt(LocalDateTime.now());

        return toResponse(u);
    }

    @Transactional
    public void delete(Long id){
        if (!userRepo.existsById(id)) throw new NotFoundException("Usuario no encontrado");
        userRepo.deleteById(id);
    }

    private UserResponse toResponse(User u){
        return new UserResponse(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getRole().getName(),
                u.isEnabled()
        );
    }
}