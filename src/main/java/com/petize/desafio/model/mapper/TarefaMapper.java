package com.petize.desafio.model.mapper;

import com.petize.desafio.model.dto.tarefa.TarefaCreateDto;
import com.petize.desafio.model.dto.tarefa.TarefaDto;
import com.petize.desafio.model.entity.Tarefa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TarefaMapper {

    TarefaDto toDto(Tarefa tarefa);

    @Mapping(target = "idTarefa", ignore = true)
    @Mapping(target = "subtarefas", source = "subtarefas")
    Tarefa toEntity(TarefaCreateDto tarefaCreateDto);

}
