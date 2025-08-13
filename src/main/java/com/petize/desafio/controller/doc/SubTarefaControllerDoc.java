package com.petize.desafio.controller.doc;

import com.petize.desafio.model.dto.error.ErrorResponseDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaCreateDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaStatusDto;
import com.petize.desafio.model.dto.subtarefa.SubTarefaUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SubTarefas", description = "Operações para gerenciar SubTarefas de uma Tarefa")
public interface SubTarefaControllerDoc {

    @Operation(
            summary = "Criar subtarefa para uma tarefa",
            parameters = {
                    @Parameter(name = "idTarefa", in = ParameterIn.PATH, required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubTarefaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @PostMapping("/tarefas/{idTarefa}/subtarefas")
    ResponseEntity<SubTarefaDto> criarSubTarefa(@PathVariable("idTarefa") Long idTarefa, @RequestBody SubTarefaCreateDto body);

    @Operation(
            summary = "Buscar subtarefa por ID",
            parameters = {
                    @Parameter(name = "idSubTarefa", in = ParameterIn.PATH, required = true, example = "10")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubTarefaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @GetMapping("/tarefas/subtarefas/{idSubTarefa}")
    ResponseEntity<SubTarefaDto> buscarSubTarefaPorId(@PathVariable("idSubTarefa") Long idSubTarefa);

    @Operation(
            summary = "Listar subtarefas de uma tarefa",
            parameters = {
                    @Parameter(name = "idTarefa", in = ParameterIn.PATH, required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SubTarefaDto.class))))
            }
    )
    @GetMapping("/tarefas/{idTarefa}/subtarefas")
    ResponseEntity<List<SubTarefaDto>> listarPorTarefa(@PathVariable("idTarefa") Long idTarefa);

    @Operation(
            summary = "Atualizar título/atributos da subtarefa (PATCH)",
            description = "Atualiza campos editáveis de uma subtarefa (ex.: título).",
            parameters = {
                    @Parameter(name = "idSubTarefa", in = ParameterIn.PATH, required = true, example = "10")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Atualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubTarefaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @PatchMapping("/tarefas/subtarefas/{idSubTarefa}")
    ResponseEntity<SubTarefaDto> atualizarSubTarefa(@PathVariable("idSubTarefa") Long idSubTarefa, @RequestBody SubTarefaUpdateDto body);

    @Operation(
            summary = "Atualizar status da subtarefa (PATCH)",
            parameters = {
                    @Parameter(name = "idSubTarefa", in = ParameterIn.PATH, required = true, example = "10")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Atualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SubTarefaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @PatchMapping("/tarefas/subtarefas/{idSubTarefa}/status")
    ResponseEntity<SubTarefaDto> atualizarStatusSubTarefa(@PathVariable("idSubTarefa") Long idSubTarefa, @RequestBody SubTarefaStatusDto body);

    @Operation(
            summary = "Excluir subtarefa",
            parameters = {
                    @Parameter(name = "idSubTarefa", in = ParameterIn.PATH, required = true, example = "10")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Excluída"),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    @DeleteMapping("/tarefas/subtarefas/{idSubTarefa}")
    ResponseEntity<Void> deletarSubTarefa(@PathVariable("idSubTarefa") Long idSubTarefa);
}