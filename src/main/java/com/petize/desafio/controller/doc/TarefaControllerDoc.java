package com.petize.desafio.controller.doc;

import com.petize.desafio.model.dto.error.ErrorResponseDto;
import com.petize.desafio.model.dto.tarefa.*;
import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Tarefas", description = "Operações para gerenciar Tarefas")
public interface TarefaControllerDoc {

    @Operation(
            summary = "Criar tarefa",
            description = "Cria uma nova tarefa.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TarefaDto.class),
                                    examples = @ExampleObject(value = """
                    {
                      "idTarefa": 1,
                      "tituloTarefa": "Estudar Spring",
                      "descricao": "Avançar no módulo de validação",
                      "dataVencimento": "2025-08-20",
                      "status": "PENDENTE",
                      "prioridade": "ALTA"
                    }
                    """)
                            )),
                    @ApiResponse(responseCode = "400", description = "Erro de validação",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    ResponseEntity<TarefaDto> criarTarefa(TarefaCreateDto tarefaCreateDto);

    @Operation(
            summary = "Buscar tarefa por ID",
            parameters = {
                    @Parameter(name = "idTarefa", in = ParameterIn.PATH, required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TarefaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    ResponseEntity<TarefaDto> buscarTarefaPorId(@PathVariable Long idTarefa);

    @Operation(
            summary = "Listar tarefas (não paginado)",
            description = "Lista tarefas com filtros por status, prioridade e data de vencimento (exata).",
            parameters = {
                    @Parameter(name = "status", in = ParameterIn.QUERY, schema = @Schema(implementation = Status.class)),
                    @Parameter(name = "prioridade", in = ParameterIn.QUERY, schema = @Schema(implementation = Prioridade.class)),
                    @Parameter(name = "dataVencimento", in = ParameterIn.QUERY, description = "Data exata de vencimento", example = "2025-08-20")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ok",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TarefaPaginacaoDto.class)))
            }
    )
    ResponseEntity<List<TarefaDto>> listar(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataVencimento
    );

    @Operation(
            summary = "Listar tarefas completas",
            description = "Busca todas as tarefas com suas subtarefas agregadas. "
                    + "Se informado, filtra pelo ID da tarefa."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    ResponseEntity<List<TarefaPaginacaoDto>> buscarTarefasCompleto(
            @Parameter(
                    name = "idTarefa",
                    in = ParameterIn.QUERY,
                    required = false,
                    description = "Filtra por ID da tarefa",
                    example = "1"
            )
            @RequestParam Optional<Long> idTarefa
    );

    @Operation(
            summary = "Atualizar tarefa (PATCH)",
            description = "Atualiza campos editáveis da tarefa (**Título**,**Descrição** ,**Data de Vencimento**, **Prioridade**).",
            parameters = {
                    @Parameter(name = "idTarefa", in = ParameterIn.PATH, required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Atualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TarefaDto.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    ResponseEntity<TarefaDto> atualizarTarefa(@PathVariable Long idTarefa, TarefaUpdateDto tarefaUpdateDto);

    @Operation(
            summary = "Atualizar status da tarefa (PATCH)",
            description = "Altera apenas o status. **Regra:** impedir conclusão se houver subtarefas pendentes.",
            parameters = {
                    @Parameter(name = "idTarefa", in = ParameterIn.PATH, required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TarefaDto.class))),
                    @ApiResponse(responseCode = "409", description = "Conflito de regra de negócio (há subtarefas pendentes)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    ResponseEntity<TarefaDto> atualizarStatusTarefa(@PathVariable Long idTarefa, TarefaStatusDto tarefaStatusDto);

    @Operation(
            summary = "Excluir tarefa",
            parameters = {
                    @Parameter(name = "idTarefa", in = ParameterIn.PATH, required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Excluída"),
                    @ApiResponse(responseCode = "404", description = "Não encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Entidade em uso / violação de regra",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponseDto.class)))
            }
    )
    ResponseEntity<Void> deletarTarefa(@PathVariable Long idTarefa);
}
