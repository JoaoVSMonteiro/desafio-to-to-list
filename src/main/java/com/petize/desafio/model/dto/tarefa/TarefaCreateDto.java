package com.petize.desafio.model.dto.tarefa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaCreateDto {
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 150, message = "título deve ter no máximo 150 caracteres")
    @Schema(example = "Estudar Spring")
    private String tituloTarefa;

    @Size(max = 1000, message = "descrição deve ter no máximo 1000 caracteres")
    @Schema(example = "Avançar no módulo de validação")
    private String descricao;

    @NotNull(message = "A data de vencimento é obrigatória")
    @FutureOrPresent(message = "A data deve ser hoje ou no futuro")
    @Schema(type = "string", format = "date", example = "2025-08-20")
    private LocalDate dataVencimento;

    @NotNull(message = "O status é obrigatório")
    @Schema(implementation = Status.class, example = "PENDENTE")
    private Status status;

    @NotNull(message = "A prioridade é obrigatória")
    @Schema(implementation = Prioridade.class, example = "ALTA")
    private Prioridade prioridade;

}
