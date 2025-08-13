package com.petize.desafio.model.dto.tarefa;

import com.petize.desafio.model.enums.Prioridade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaUpdateDto {
    @Size(max = 150, message = "título deve ter no máximo 150 caracteres")
    @Schema(example = "Estudar Spring Cap. 2")
    private String tituloTarefa;

    @Size(max = 1000, message = "descrição deve ter no máximo 1000 caracteres")
    @Schema(example = "Avançar no módulo de validação")
    private String descricao;

    @FutureOrPresent(message = "A data deve ser hoje ou no futuro")
    @Schema(type = "string", format = "date", example = "2025-08-20")
    private LocalDate dataVencimento;

    @Schema(implementation = Prioridade.class, example = "ALTA")
    private Prioridade prioridade;
}
