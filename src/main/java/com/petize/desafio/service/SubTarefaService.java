package com.petize.desafio.service;

import com.petize.desafio.repository.SubTarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubTarefaService {

    private final SubTarefaRepository subTarefaRepository;
}
