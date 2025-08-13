package com.petize.desafio.model.dto.subtarefa;

import com.petize.desafio.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubTarefaCreateDto {
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 150, message = "título da subtarefa deve ter no máximo 150 caracteres")
    @Schema(example = "Estudar Spring Boot")
    private String tituloSubTarefa;

    @NotNull(message = "O status é obrigatório")
    @Schema(implementation = Status.class, example = "PENDENTE")
    private Status status;
}
