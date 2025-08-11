package com.petize.desafio.model.dto.subtarefa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.petize.desafio.model.enums.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubTarefaDto {

    private Long idSubTarefa;

    private String tituloSubTarefa;

    private Status status;
}
