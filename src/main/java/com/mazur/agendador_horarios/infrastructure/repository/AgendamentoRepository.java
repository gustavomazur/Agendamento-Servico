package com.mazur.agendador_horarios.infrastructure.repository;

import com.mazur.agendador_horarios.infrastructure.entity.AgendamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoEntity, Long> {

    List<AgendamentoEntity> findByServicoAndDataHoraAgendamentoBetween(String servico,
                                                                       LocalDateTime horaAgendamento,
                                                                       LocalDateTime horaFim);

    @Transactional
    void deleteByDataHoraAgendamentoAndCliente(LocalDateTime dataHoraAgendamento, String cliente);

    List<AgendamentoEntity> findByDataHoraAgendamentoBetween(LocalDateTime dataHoraInicial,
                                                             LocalDateTime dataHoraFinal);

    AgendamentoEntity findByDataHoraAgendamentoAndCliente(LocalDateTime dataHoraAgendamento,
                                                          String cliente);

}
