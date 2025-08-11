package com.petize.desafio.service;

import com.petize.desafio.model.dto.tarefa.TarefaCreateDto;
import com.petize.desafio.model.dto.tarefa.TarefaDto;
import com.petize.desafio.model.dto.tarefa.TarefaStatusDto;
import com.petize.desafio.model.dto.tarefa.TarefaUpdateDto;
import com.petize.desafio.model.entity.Tarefa;
import com.petize.desafio.model.enums.Status;
import com.petize.desafio.model.mapper.TarefaMapper;
import com.petize.desafio.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;
    private final SubTarefaService subTarefaService;

    @Transactional
    public TarefaDto criarTarefa(TarefaCreateDto tarefaCreateDto){
        log.info("Iniciando criação de tarefa");

        Tarefa tarefa = tarefaMapper.toEntity(tarefaCreateDto);
        if (tarefa.getStatus() == null) tarefa.setStatus(Status.PENDENTE);
        tarefa = tarefaRepository.save(tarefa);

        log.info("Tarefa criada com sucesso. ID={}", tarefa.getIdTarefa());
        return tarefaMapper.toDto(tarefa);
    }

    @Transactional(readOnly = true)
    public TarefaDto buscarTarefaPorId(Long idTarefa){
        return tarefaRepository.findById(idTarefa)
                .map(tarefa -> {
                    log.info("Tarefa encontrada. ID = {}", idTarefa);
                    return  tarefaMapper.toDto(tarefa);
                }).orElseThrow(() -> {
                    log.warn("Tarefa não encontrada. ID = {}", idTarefa);
                    return new EntityNotFoundException("Tarefa com id = " + idTarefa + " não encontrada");
                });
    }

    @Transactional
    public TarefaDto atualizarTarefa(Long idTarefa, TarefaUpdateDto tarefaUpdateDto){
        log.info("Iniciando atualização de tarefa");

        Tarefa tarefaAtualizar = buscarTarefaById(idTarefa);

        tarefaMapper.atualizarTarefaMapper(tarefaUpdateDto, tarefaAtualizar);

        tarefaRepository.save(tarefaAtualizar);

        return tarefaMapper.toDto(tarefaAtualizar);
    }

    @Transactional
    public TarefaDto atualizarStatusTarefa(Long idTarefa, TarefaStatusDto tarefaStatusDto){
        log.info("Atualizando status da tarefa: {}...", idTarefa);

        Tarefa tarefa = buscarTarefaById(idTarefa);
        Status novoStatus = tarefaStatusDto.getStatus();

        if (novoStatus == tarefa.getStatus()) {
            log.info("Status da tarefa {} já é {}", idTarefa, novoStatus);
            return tarefaMapper.toDto(tarefa);
        }

        if(novoStatus == Status.CONCLUIDA) {
            boolean existePendente = subTarefaService.possuiPendentes(idTarefa);
            if(existePendente){
                throw new IllegalStateException("Não é possível concluir, existem subtarefas pendentes");
            }
        }
        tarefa.setStatus(novoStatus);
        log.info("Status da tarefa {} atualizado para {}", idTarefa, novoStatus);
        return tarefaMapper.toDto(tarefa);
    }

    @Transactional
    public void deletarTarefa(Long idTarefa){
        if(!tarefaRepository.existsById(idTarefa)){
            log.warn("Tarefa com ID= {}, não encontrada", idTarefa);
            throw new EntityNotFoundException("Tarefa com id = " + idTarefa + " não encontrada");
        }
        tarefaRepository.deleteById(idTarefa);
        log.info("Tarefa deletada com sucesso. ID={}", idTarefa);
    }

    @Transactional(readOnly = true)
    public Tarefa buscarTarefaById(Long idTarefa){
        return tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id = " + idTarefa + " não encontrada"));
    }



}
