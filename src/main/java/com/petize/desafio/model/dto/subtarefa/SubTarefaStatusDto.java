package com.petize.desafio.model.dto.subtarefa;

import com.petize.desafio.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubTarefaStatusDto {
    @NotNull(message = "O status é obrigatório")
    @Schema(implementation = Status.class, example = "PENDENTE")
    private Status status;
}
