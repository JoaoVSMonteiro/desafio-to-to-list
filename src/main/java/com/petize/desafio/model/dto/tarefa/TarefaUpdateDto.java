package com.petize.desafio.model.dto.tarefa;

import com.petize.desafio.model.enums.Prioridade;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaUpdateDto {
    private String tituloTarefa;

    private String descricao;

    private LocalDate dataVencimento;

    private Prioridade prioridade;
}
