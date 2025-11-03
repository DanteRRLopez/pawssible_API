package com.bootcamp.pawssible_API.usuario.dto;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String roleName,
        Boolean enabled
) {
}
