package com.mazur.agendador_horarios.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.mazur.agendador_horarios.InvalidGoogleTokenException;
import com.mazur.agendador_horarios.infrastructure.dto.LoginResponseDTO;
import com.mazur.agendador_horarios.infrastructure.dto.UsuarioDTO;
import com.mazur.agendador_horarios.infrastructure.dto.UsuarioResponseDTO;
import com.mazur.agendador_horarios.infrastructure.enums.Role;
import com.mazur.agendador_horarios.infrastructure.mapper.UsuarioMapper;
import com.mazur.agendador_horarios.infrastructure.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final GoogleIdTokenVerifier verifier;
    private final JwtService jwtService;
    @Value("${app.admin-emails:}")
    private List<String> adminEmails;


    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          GoogleIdTokenVerifier verifier,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.verifier = verifier;
        this.jwtService = jwtService;
    }

    @Transactional
    public UsuarioDTO createUser(UsuarioDTO usuarioDTO) {
        return usuarioMapper.paraDTO(usuarioRepository.save(usuarioMapper.paraEntity(usuarioDTO)));
    }

    @Transactional
    public Optional<UsuarioDTO> findUserBySub(String sub) {
        return usuarioRepository.findBySub(sub).map(usuarioMapper::paraDTO);
    }

    private GoogleIdToken.Payload verifyToken(String credential) {
        try {
            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken == null) {
                throw new InvalidGoogleTokenException("Invalid token.");
            }
            return idToken.getPayload();
        } catch (GeneralSecurityException | IOException e) {
            throw new InvalidGoogleTokenException(e.getMessage());
        }
    }

    private String stringOrEmpty(Object value) {
        return value == null ? "" : value.toString();
    }

    private void sincronizarRole(String sub, String email, boolean emailVerificado) {
        usuarioRepository.findBySub(sub).ifPresent(u -> {
            boolean admin = emailVerificado && adminEmails.contains(email);
            Role desejado = admin ? Role.ADMIN : Role.USER;

            if (u.getRole() != desejado) {
                u.setRole(desejado);
            }
        });
    }

    @Transactional
    public UsuarioResponseDTO loginWithGoogle(String credential) {
        GoogleIdToken.Payload payload = verifyToken(credential);

        UsuarioDTO usuario = findUserBySub(payload.getSubject())
                .orElseGet(() -> createUser(new UsuarioDTO(
                        stringOrEmpty(payload.get("picture")),
                        stringOrEmpty(payload.get("name")),
                        stringOrEmpty(payload.getEmail()),
                        stringOrEmpty(payload.getSubject())
                )));

        sincronizarRole(
                usuario.sub(),
                usuario.email(),
                Boolean.TRUE.equals(payload.getEmailVerified())
        );

        String token = jwtService.generateToken(usuario.sub());
        return new LoginResponseDTO(usuarioMapper.paraResponse(usuario), token);
    }
}


