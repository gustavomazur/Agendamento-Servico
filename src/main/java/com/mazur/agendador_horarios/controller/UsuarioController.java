package com.mazur.agendador_horarios.controller;

import com.mazur.agendador_horarios.InvalidGoogleTokenException;
import com.mazur.agendador_horarios.infrastructure.dto.UsuarioDTO;
import com.mazur.agendador_horarios.infrastructure.dto.UsuarioResponseDTO;
import com.mazur.agendador_horarios.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/google")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public record LoginRequest(@NotBlank(message = "Token not sent.") String credential) {}

    @PostMapping("/login")
    public UsuarioResponseDTO login(@Valid @RequestBody LoginRequest request) {
        return usuarioService.loginWithGoogle(request.credential());
    }

    @ExceptionHandler(InvalidGoogleTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidToken(InvalidGoogleTokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid token.", "message", e.getMessage()));
    }
}

