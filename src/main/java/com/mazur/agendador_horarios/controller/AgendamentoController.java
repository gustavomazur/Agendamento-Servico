package com.mazur.agendador_horarios.controller;

import com.mazur.agendador_horarios.infrastructure.dto.AlteraAgendamentoDTO;
import com.mazur.agendador_horarios.infrastructure.entity.AgendamentoEntity;
import com.mazur.agendador_horarios.service.AgendamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/usuarios/{sub}")
    @PreAuthorize("hasRole('ADMIN') or #sub == authentication.name")
    public ResponseEntity<AgendamentoEntity> salvarAgendamento(
            @RequestBody AgendamentoEntity agendamentoEntity) {
        return ResponseEntity.accepted().body(agendamentoService.salvarAgendamento(agendamentoEntity));
    }

    @PutMapping("/usuarios/{sub}")
    @PreAuthorize("hasRole('ADMIN') or #sub == authentication.name")
    public ResponseEntity<Void> deletarAgendamento(
            @RequestParam String cliente,
            @RequestParam LocalDateTime dataHoraAgendamento) {
        agendamentoService.deletarAgendamento(dataHoraAgendamento, cliente);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuarios/{sub}")
    @PreAuthorize("hasRole('ADMIN') or #sub == authentication.name")
    public ResponseEntity<List<AgendamentoEntity>> buscarAgendamentosDia(@RequestParam LocalDate data) {
        return ResponseEntity.ok().body(agendamentoService.buscarAgendamentoDia(data));
    }

    @PutMapping("/usuarios/{sub}")
    @PreAuthorize("hasRole('ADMIN') or #sub == authentication.name")
    public ResponseEntity<AgendamentoEntity> alterarAgendamento(@RequestBody AlteraAgendamentoDTO dto) {
        return ResponseEntity.accepted().body(agendamentoService.alterarAgendamentoDto(dto));
    }
}
