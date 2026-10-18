package com.mazur.agendador_horarios.service;

import com.mazur.agendador_horarios.infrastructure.dto.AlteraAgendamentoDTO;
import com.mazur.agendador_horarios.infrastructure.entity.AgendamentoEntity;
import com.mazur.agendador_horarios.infrastructure.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoEntity salvarAgendamento(AgendamentoEntity agendamento) {
        LocalDateTime horaAgendamento = agendamento.getDataHoraAgendamento();
        LocalDateTime horaFim = horaAgendamento.plusHours(1);

        List<AgendamentoEntity> agendados = agendamentoRepository.findByServicoAndDataHoraAgendamentoBetween(
                agendamento.getServico(), horaAgendamento, horaFim
        );

        if (!agendados.isEmpty()) {
            throw new RuntimeException("Horário já está preenchido");
        }

        return agendamentoRepository.save(agendamento);
    }

    public void deletarAgendamento(LocalDateTime dataHoraAgendamento, String cliente) {
        agendamentoRepository.deleteByDataHoraAgendamentoAndCliente(dataHoraAgendamento, cliente);
    }

    public List<AgendamentoEntity> buscarAgendamentoDia(LocalDate data) {
        LocalDateTime primeiraHoraDia = data.atStartOfDay();
        LocalDateTime horaFinalDia = data.atTime(23, 59, 59);

        return agendamentoRepository.findByDataHoraAgendamentoBetween(primeiraHoraDia, horaFinalDia);
    }

    public AgendamentoEntity alterarAgendamentoDto(AlteraAgendamentoDTO dto) {

        AgendamentoEntity agenda = agendamentoRepository.findByDataHoraAgendamentoAndCliente(
                dto.dataHoraAntiga(), dto.cliente()
        );

        if (Objects.isNull(agenda)) {
            throw new RuntimeException("Agendamento não encontrado para este cliente nesta data.");
        }

        agenda.setServico(dto.servico());
        agenda.setProfissional(dto.profissional());
        agenda.setDataHoraAgendamento(dto.dataHoraNova());
        agenda.setTelefoneCliente(dto.telefoneCliente());

        return agendamentoRepository.save(agenda);
    }

}
