package com.petize.desafio.model.dto.tarefa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaDto {

    @Schema(example = "1")
    private Long idTarefa;

    @Schema(example = "Estudar Spring")
    private String tituloTarefa;

    @Schema(example = "Avançar no módulo de validação")
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-08-20")
    private LocalDate dataVencimento;

    @Schema(implementation = Status.class, example = "PENDENTE")
    private Status status;

    @Schema(implementation = Prioridade.class, example = "ALTA")
    private Prioridade prioridade;
}
