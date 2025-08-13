package com.petize.desafio.service;

import com.petize.desafio.exception.RecursoNaoEncontradoException;
import com.petize.desafio.exception.RegraDeNegocioException;
import com.petize.desafio.model.dto.subtarefa.SubTarefaDto;
import com.petize.desafio.model.dto.tarefa.*;
import com.petize.desafio.model.entity.Tarefa;
import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import com.petize.desafio.model.mapper.TarefaMapper;
import com.petize.desafio.repository.TarefaRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

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

        String tituloTarefa = tarefaCreateDto.getTituloTarefa().trim();
        if(tarefaRepository.existsByTituloTarefaIgnoreCase(tarefaCreateDto.getTituloTarefa())){
            throw new RegraDeNegocioException("Já existe uma tarefa com o título: " + tarefaCreateDto.getTituloTarefa());
        }

        Tarefa tarefa = tarefaMapper.toEntity(tarefaCreateDto);
        if (tarefa.getStatus() == null
                || tarefa.getStatus() == Status.CONCLUIDA
                || tarefa.getStatus() == Status.CANCELADA ) {
            tarefa.setStatus(Status.PENDENTE);
        }
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
                    return new RecursoNaoEncontradoException("Tarefa", idTarefa);
                });
    }

    @Transactional(readOnly = true)
    public List<TarefaDto> listarTarefas(Status status, Prioridade prioridade, LocalDate dataVencimento) {
        List<Tarefa> tarefas = tarefaRepository.findByFiltros(status, prioridade, dataVencimento);
        return tarefas.stream().map(tarefaMapper::toDto).toList();
    }


    @Transactional(readOnly = true)
    public List<TarefaPaginacaoDto> listarTarefasCompleto(Optional<Long> idTarefaFiltro) {
        List<Object[]> rows = tarefaRepository.buscaTarefaCompleto();
        List<TarefaPaginacaoDto> lista = processarBuscaTarefaCompleto(rows);

        return idTarefaFiltro
                .map(id -> lista.stream()
                        .filter(t -> t.getIdTarefa().equals(id))
                        .toList())
                .orElse(lista);
    }

    public List<TarefaPaginacaoDto> processarBuscaTarefaCompleto(List<Object[]> lista) {
        Map<Long, TarefaPaginacaoDto> map = new LinkedHashMap<>();

        for (Object[] r : lista) {
            Long idTarefa             = (Long)      r[0];
            String tituloTarefa       = (String)    r[1];
            String descricao          = (String)    r[2];
            LocalDate dataVencimento  = (LocalDate) r[3];
            Status status             = (Status)    r[4];
            Prioridade prioridade     = (Prioridade)r[5];
            Long idSubTarefa          = (Long)      r[6];
            String tituloSubTarefa    = (String)    r[7];
            Status statusSub          = (Status)    r[8];

            TarefaPaginacaoDto agg = map.computeIfAbsent(idTarefa, id ->
                    TarefaPaginacaoDto.builder()
                            .idTarefa(idTarefa)
                            .tituloTarefa(tituloTarefa)
                            .descricao(descricao)
                            .dataVencimento(dataVencimento)
                            .status(status)
                            .prioridade(prioridade)
                            .subtarefas(new LinkedHashSet<>())
                            .build()
            );

            if (idSubTarefa != null) {
                agg.getSubtarefas().add(
                        SubTarefaDto.builder()
                                .idSubTarefa(idSubTarefa)
                                .tituloSubTarefa(tituloSubTarefa)
                                .status(statusSub)
                                .build()
                );
            }
        }

        return new ArrayList<>(map.values());
    }

    @Transactional
    public TarefaDto atualizarTarefa(Long idTarefa, TarefaUpdateDto tarefaUpdateDto){
        log.info("Iniciando atualização de tarefa");

        Tarefa tarefaAtualizar = buscarTarefaById(idTarefa);

        tarefaMapper.atualizarTarefaMapper(tarefaUpdateDto, tarefaAtualizar);

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
                log.error("Não é possível concluir tarefa, existem subtarefas pendentes");
                throw new RegraDeNegocioException("Não é possível concluir tarefa, existem subtarefas pendentes");
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
            throw new RecursoNaoEncontradoException("Tarefa", idTarefa);
        }
        tarefaRepository.deleteById(idTarefa);
        log.info("Tarefa deletada com sucesso. ID={}", idTarefa);
    }

    @Transactional(readOnly = true)
    public Tarefa buscarTarefaById(Long idTarefa){
        return tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa", idTarefa));
    }



}
