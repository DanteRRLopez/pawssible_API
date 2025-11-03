package com.bootcamp.pawssible_API.usuario.dto;

import jakarta.validation.constraints.*;

public record UserCreateRequest(@NotBlank @Size(max=80) String firstName,
                                @NotBlank @Size(max=80) String lastName,
                                @Email @NotBlank @Size(max=120) String email,
                                @NotBlank @Size(min=6, max=64) String password,
                                @NotNull Long roleId,
                                Boolean enabled)
{


}
