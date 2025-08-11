package com.petize.desafio.controller;

import com.petize.desafio.model.dto.tarefa.TarefaCreateDto;
import com.petize.desafio.model.dto.tarefa.TarefaDto;
import com.petize.desafio.model.dto.tarefa.TarefaStatusDto;
import com.petize.desafio.model.dto.tarefa.TarefaUpdateDto;
import com.petize.desafio.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefa")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaDto> criarTarefa(@Valid @RequestBody TarefaCreateDto tarefaCreateDto){
        TarefaDto tarefaDto = tarefaService.criarTarefa(tarefaCreateDto);
        return new ResponseEntity<>(tarefaDto, HttpStatus.CREATED);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<TarefaDto> buscarTarefaPorId(@PathVariable("id") Long idTarefa){
        return ResponseEntity.ok(tarefaService.buscarTarefaPorId(idTarefa));
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<TarefaDto> atualizarTarefa(@PathVariable ("id") Long idTarefa,@Valid @RequestBody TarefaUpdateDto tarefaUpdateDto) {
        TarefaDto tarefaDto = tarefaService.atualizarTarefa(idTarefa, tarefaUpdateDto);
        return new ResponseEntity<>(tarefaDto, HttpStatus.OK);
    }

    @PatchMapping("status/id/{id}")
    public ResponseEntity<TarefaDto> atualizarStatisTarefa (@PathVariable ("id") Long idTarefa,@Valid @RequestBody TarefaStatusDto tarefaStatusDto){
        TarefaDto tarefaDto = tarefaService.atualizarStatusTarefa(idTarefa, tarefaStatusDto);
        return new ResponseEntity<>(tarefaDto, HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable("id") Long idTarefa){
        tarefaService.deletarTarefa(idTarefa);
        return ResponseEntity.noContent().build();
    }
}
