package com.petize.desafio.controller;

import com.petize.desafio.model.dto.tarefa.*;
import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import com.petize.desafio.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaDto> criarTarefa(@Valid @RequestBody TarefaCreateDto tarefaCreateDto){
        TarefaDto tarefaDto = tarefaService.criarTarefa(tarefaCreateDto);
        return new ResponseEntity<>(tarefaDto, HttpStatus.CREATED);
    }

    @GetMapping("/{idTarefa}")
    public ResponseEntity<TarefaDto> buscarTarefaPorId(@PathVariable Long idTarefa) {
        return ResponseEntity.ok(tarefaService.buscarTarefaPorId(idTarefa));
    }

    @GetMapping
    public ResponseEntity<List<TarefaDto>> listar(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVencimento) {
        return ResponseEntity.ok(tarefaService.listarTarefas(status, prioridade, dataVencimento));
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<TarefaPaginacaoDto>> listaPaginada(
            @RequestParam(required = false) Long idTarefa,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVencimento,
            @PageableDefault(size = 10, sort = "dataVencimento") Pageable pageable
    ){
        Page<TarefaPaginacaoDto> pagina = tarefaService.listarTarefasPaginado(Optional.ofNullable(idTarefa), status, prioridade, dataVencimento, pageable);
        return ResponseEntity.ok(pagina);
    }

    @PatchMapping("/{idTarefa}")
    public ResponseEntity<TarefaDto> atualizarTarefa(@PathVariable Long idTarefa, @Valid @RequestBody TarefaUpdateDto tarefaUpdateDto) {
        TarefaDto tarefaDto = tarefaService.atualizarTarefa(idTarefa, tarefaUpdateDto);
        return new ResponseEntity<>(tarefaDto, HttpStatus.OK);
    }

    @PatchMapping("/{idTarefa}/status")
    public ResponseEntity<TarefaDto> atualizarStatusTarefa(@PathVariable Long idTarefa, @Valid @RequestBody TarefaStatusDto tarefaStatusDto) {
        TarefaDto tarefaDto = tarefaService.atualizarStatusTarefa(idTarefa, tarefaStatusDto);
        return new ResponseEntity<>(tarefaDto, HttpStatus.OK);
    }

    @DeleteMapping("/{idTarefa}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long idTarefa) {
        tarefaService.deletarTarefa(idTarefa);
        return ResponseEntity.noContent().build();
    }
}
