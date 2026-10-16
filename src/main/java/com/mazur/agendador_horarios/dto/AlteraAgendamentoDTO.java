package com.mazur.agendador_horarios.dto;

import java.time.LocalDateTime;

public record AlteraAgendamentoDTO(
        String cliente,
        LocalDateTime dataHoraAntiga,
        String servico,
        LocalDateTime dataHoraNova,
        String profissional,
        String telefoneCliente
) {
}
