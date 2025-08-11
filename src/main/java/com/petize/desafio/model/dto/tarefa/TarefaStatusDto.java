package com.petize.desafio.model.dto.tarefa;

import com.petize.desafio.model.enums.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaStatusDto {

    private Status status;
}
