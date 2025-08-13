package com.petize.desafio.controller;

import com.petize.desafio.controller.doc.SubTarefaControllerDoc;
import com.petize.desafio.controller.doc.TarefaControllerDoc;
import com.petize.desafio.model.dto.subtarefa.SubTarefaCreateDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaStatusDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaUpdateDto;
import com.petize.desafio.service.SubTarefaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class SubTarefaController implements SubTarefaControllerDoc {

    private final SubTarefaService subTarefaService;

    @PostMapping("/{idTarefa}/subtarefas")
    public ResponseEntity<SubTarefaDto> criarSubTarefa(@PathVariable Long idTarefa, @Valid @RequestBody SubTarefaCreateDto subTarefaCreateDto) {
        SubTarefaDto subTarefaDto = subTarefaService.criarSubTarefa(idTarefa, subTarefaCreateDto);
        return new ResponseEntity<>(subTarefaDto, HttpStatus.CREATED);
    }

    @GetMapping("/subtarefas/{idSubTarefa}")
    public ResponseEntity<SubTarefaDto> buscarSubTarefaPorId(@PathVariable Long idSubTarefa) {
        return ResponseEntity.ok(subTarefaService.buscarSubTarefaPorId(idSubTarefa));
    }

    @GetMapping("/{idTarefa}/subtarefas")
    public ResponseEntity<List<SubTarefaDto>> listarPorTarefa(@PathVariable Long idTarefa) {
        return ResponseEntity.ok(subTarefaService.listarPorTarefa(idTarefa));
    }

    @PatchMapping("/subtarefas/{idSubTarefa}")
    public ResponseEntity<SubTarefaDto> atualizarSubTarefa(@PathVariable Long idSubTarefa, @Valid @RequestBody SubTarefaUpdateDto subTarefaUpdateDto) {
        SubTarefaDto subTarefaDto = subTarefaService.atualizarTituloSubTarefa(idSubTarefa, subTarefaUpdateDto);
        return new ResponseEntity<>(subTarefaDto, HttpStatus.OK);
    }

    @PatchMapping("/subtarefas/{idSubTarefa}/status")
    public ResponseEntity<SubTarefaDto> atualizarStatusSubTarefa(@PathVariable Long idSubTarefa, @Valid @RequestBody SubTarefaStatusDto subTarefaStatusDto) {
        SubTarefaDto subTarefaDto = subTarefaService.atualizarStatusSubTarefa(idSubTarefa, subTarefaStatusDto);
        return new ResponseEntity<>(subTarefaDto, HttpStatus.OK);
    }

    @DeleteMapping("/subtarefas/{idSubTarefa}")
    public ResponseEntity<Void> deletarSubTarefa(@PathVariable Long idSubTarefa) {
        subTarefaService.deletarSubTarefa(idSubTarefa);
        return ResponseEntity.noContent().build();
    }
}
