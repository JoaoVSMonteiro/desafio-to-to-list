package com.petize.desafio.model.dto.tarefa;

import com.petize.desafio.model.dto.subtarefa.SubTarefaDto;
import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaPaginacaoDto {
    private Long idTarefa;
    private String tituloTarefa;
    private String descricao;
    private LocalDate dataVencimento;
    private Status status;
    private Prioridade prioridade;
    private Set<SubTarefaDto> subtarefas;
}
