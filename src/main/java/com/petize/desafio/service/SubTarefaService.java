package com.petize.desafio.service;

import com.petize.desafio.model.enums.Status;
import com.petize.desafio.repository.SubTarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubTarefaService {

    private final SubTarefaRepository subTarefaRepository;

    @Transactional(readOnly = true)
    public boolean possuiPendentes (Long idTarefa){
        return subTarefaRepository.existsByTarefa_IdTarefaAndStatusNot(idTarefa, Status.CONCLUIDA);
    }
}
