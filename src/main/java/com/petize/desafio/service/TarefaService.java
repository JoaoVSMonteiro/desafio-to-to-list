package com.petize.desafio.service;

import com.petize.desafio.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TarefaService {

    private final TarefaRepository tarefaRepository;

}
