package com.petize.desafio.model.dto.tarefa;

import com.petize.desafio.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaStatusDto {

    @NotNull(message = "O status é obrigatório")
    @Schema(implementation = Status.class, example = "PENDENTE")
    private Status status;
}
