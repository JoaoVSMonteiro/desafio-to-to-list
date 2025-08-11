package com.petize.desafio.model.mapper;

import com.petize.desafio.model.dto.tarefa.TarefaCreateDto;
import com.petize.desafio.model.dto.tarefa.TarefaDto;
import com.petize.desafio.model.dto.tarefa.TarefaUpdateDto;
import com.petize.desafio.model.entity.Tarefa;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface TarefaMapper {

    TarefaDto toDto(Tarefa tarefa);

    @Mapping(target = "idTarefa", ignore = true)
    Tarefa toEntity(TarefaCreateDto tarefaCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizarTarefaMapper(TarefaUpdateDto tarefaUpdateDto, @MappingTarget Tarefa tarefa);
}
