package com.petize.desafio.model.mapper;

import com.petize.desafio.model.dto.subtarefa.SubTarefaCreateDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaUpdateDto;
import com.petize.desafio.model.entity.Subtarefa;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubTarefaMapper {

    SubTarefaDto toDto(Subtarefa subtarefa);

    @Mapping(target = "idSubTarefa", ignore = true)
    @Mapping(target = "tarefa", ignore = true)
    Subtarefa toEntity(SubTarefaCreateDto subTarefaDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void atualizarSubTarefaMapper(SubTarefaUpdateDto subTarefaUpdateDto, @MappingTarget Subtarefa subtarefa);
}
