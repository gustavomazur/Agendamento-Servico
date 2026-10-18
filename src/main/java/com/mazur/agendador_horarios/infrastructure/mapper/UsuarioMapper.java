package com.mazur.agendador_horarios.infrastructure.mapper;

import com.mazur.agendador_horarios.infrastructure.dto.UsuarioDTO;
import com.mazur.agendador_horarios.infrastructure.dto.UsuarioResponseDTO;
import com.mazur.agendador_horarios.infrastructure.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioDTO paraDTO(UsuarioEntity usuario);

    @Mapping(target = "id", ignore = true)
    UsuarioEntity paraEntity(UsuarioDTO usuarioDTO);

    UsuarioResponseDTO paraResponse(UsuarioDTO dto);

}
