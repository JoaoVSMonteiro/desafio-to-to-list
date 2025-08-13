package com.petize.desafio.model.dto.subtarefa;

import com.petize.desafio.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubTarefaUpdateDto {
    @Size(max = 150, message = "título deve ter no máximo 150 caracteres")
    @Schema(example = "Estudar Spring Cap. 2")
    private String tituloSubTarefa;
}
