package com.petize.desafio.service;

import com.petize.desafio.model.dto.subtarefa.SubTarefaCreateDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaStatusDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaUpdateDto;
import com.petize.desafio.model.entity.Subtarefa;
import com.petize.desafio.model.entity.Tarefa;
import com.petize.desafio.model.enums.Status;
import com.petize.desafio.model.mapper.SubTarefaMapper;
import com.petize.desafio.repository.SubTarefaRepository;
import com.petize.desafio.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubTarefaService {

    private final SubTarefaRepository subTarefaRepository;
    private final SubTarefaMapper subTarefaMapper;
    private final TarefaRepository tarefaRepository;

    @Transactional
    public SubTarefaDto criarSubTarefa(Long idTarefa, SubTarefaCreateDto subTarefaCreateDto){
        log.info("Iniciando criação da subtarefa");

        Tarefa idTarefaAssociada = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com ID: "+ idTarefa +" não encontrada"));

        validarStatusTarefa(idTarefaAssociada);

        Subtarefa subtarefa = subTarefaMapper.toEntity(subTarefaCreateDto);
        if (subtarefa.getStatus() == null
                || subtarefa.getStatus() == Status.CONCLUIDA
                || subtarefa.getStatus() == Status.CANCELADA ) {
            subtarefa.setStatus(Status.PENDENTE);
        }

        subtarefa.setTarefa(idTarefaAssociada);
        subtarefa = subTarefaRepository.save(subtarefa);

        log.info("Subtarefa criada com sucesso. ID={}", subtarefa.getIdSubTarefa());
        return subTarefaMapper.toDto(subtarefa);
    }

    @Transactional(readOnly = true)
    public SubTarefaDto buscarSubTarefaPorId(Long idSubTarefa){
        return subTarefaRepository.findById(idSubTarefa)
                .map(subtarefa -> {
                    log.info("Subtarefa encontrada. ID = {}", idSubTarefa);
                    return  subTarefaMapper.toDto(subtarefa);
                }).orElseThrow(() -> {
                    log.warn("Subtarefa não encontrada. ID = {}", idSubTarefa);
                    return new EntityNotFoundException("Subtarefa com id = " + idSubTarefa + " não encontrada");
                });
    }

    @Transactional(readOnly = true)
    public List<SubTarefaDto> listarPorTarefa(Long idTarefa){
        if(!tarefaRepository.existsById(idTarefa)) throw  new EntityNotFoundException("Tarefa com ID: " + idTarefa+ " não encontrada");
        return subTarefaRepository.findByTarefa_IdTarefa(idTarefa)
                .stream()
                .map(subTarefaMapper::toDto)
                .toList();
    }


    @Transactional
    public SubTarefaDto atualizarTituloSubTarefa(Long idSubTarefa, SubTarefaUpdateDto subTarefaUpdateDto){
        log.info("Iniciando atualização de subtarefa");

        Subtarefa subTarefaAtualizar = buscarSubTarefaById(idSubTarefa);

        validarStatusTarefa(subTarefaAtualizar.getTarefa());

        subTarefaMapper.atualizarSubTarefaMapper(subTarefaUpdateDto, subTarefaAtualizar);

        return subTarefaMapper.toDto(subTarefaAtualizar);
    }

    @Transactional
    public SubTarefaDto atualizarStatusSubTarefa(Long idSubTarefa, SubTarefaStatusDto subTarefaStatusDto){

        Subtarefa subtarefa = buscarSubTarefaById(idSubTarefa);
        validarStatusTarefa(subtarefa.getTarefa());
        Status novoStatus =  subTarefaStatusDto.getStatus();

        if (novoStatus == subtarefa.getStatus()) {
            log.info("Status da subtarefa {} já é {}", idSubTarefa, novoStatus);
            return subTarefaMapper.toDto(subtarefa);
        }

        subtarefa.setStatus(novoStatus);
        log.info("Status da subtarefa {} atualizado para {}", idSubTarefa, novoStatus);
        return subTarefaMapper.toDto(subtarefa);
    }

    @Transactional
    public void deletarSubTarefa(Long idSubTarefa){
        if(!subTarefaRepository.existsById(idSubTarefa)){
            log.warn("Subtarefa com ID= {}, não encontrada", idSubTarefa);
            throw new EntityNotFoundException("Subtarefa com id = " + idSubTarefa + " não encontrada");
        }
        subTarefaRepository.deleteById(idSubTarefa);
        log.info("Subtarefa deletada com sucesso. ID={}", idSubTarefa);
    }

    @Transactional(readOnly = true)
    public Subtarefa buscarSubTarefaById(Long idSubTarefa){
        return subTarefaRepository.findById(idSubTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Subtarefa com id = " + idSubTarefa + " não encontrada"));
    }

    @Transactional(readOnly = true)
    public boolean possuiPendentes (Long idTarefa){
        return subTarefaRepository.existsByTarefa_IdTarefaAndStatusNot(idTarefa, Status.CONCLUIDA);
    }

    private void validarStatusTarefa(Tarefa tarefa){
        if(tarefa.getStatus() == Status.CONCLUIDA ||tarefa.getStatus() == Status.CANCELADA){
            log.warn("Não é possível criar ou alterar subtarefas de uma tarefa já concluída ou cancelada");
            throw new IllegalStateException("Não é possível criar ou alterar subtarefas de uma tarefa já concluída ou cancelada");
        }
    }
}
