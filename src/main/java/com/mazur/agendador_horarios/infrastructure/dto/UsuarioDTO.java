package com.mazur.agendador_horarios.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public record UsuarioDTO(
        @NotBlank String picture,
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String sub
) {
}
