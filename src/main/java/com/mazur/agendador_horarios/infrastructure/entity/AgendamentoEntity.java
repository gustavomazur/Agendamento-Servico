package com.mazur.agendador_horarios.infrastructure.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String servico;
	private LocalDateTime dataHoraAgendamento;
	private String profissional;
	private String cliente;
	private String telefoneCliente;
	private LocalDateTime dataInsercao = LocalDateTime.now();

}
