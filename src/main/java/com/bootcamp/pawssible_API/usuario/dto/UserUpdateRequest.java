package com.bootcamp.pawssible_API.usuario.dto;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(
        @NotBlank @Size(max=80) String firstName,
        @NotBlank @Size(max=80) String lastName,
        @Email @NotBlank @Size(max=120) String email,
        @Size(min=6, max=64) String password, // opcional en update
        @NotNull Long roleId,
        @NotNull Boolean enabled

) {
}
