package com.mazur.agendador_horarios.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Agendador de Horários API",
                version = "1.0",
                description = "API para gestão e agendamento de horários de atendimento."
        )
)
public class OpenApiConfig {
}