package com.petize.desafio.model.dto.subtarefa;

import com.petize.desafio.model.enums.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubTarefaCreateDto {

    private String tituloSubTarefa;

    private Status status;
}
