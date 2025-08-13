package com.petize.desafio.model.dto.tarefa;

import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TarefaCreateDto {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 150, message = "título deve ter no máximo 150 caracteres")
    private String tituloTarefa;

    @Size(max = 1000, message = "descrição deve ter no máximo 1000 caracteres")
    private String descricao;

    @NotNull(message = "A data de vencimento é obrigatória")
    @FutureOrPresent(message = "A data deve ser hoje ou no futuro")
    private LocalDate dataVencimento;

    @NotNull(message = "O status é obrigatório")
    private Status status;

    @NotNull(message = "A prioridade é obrigatória")
    private Prioridade prioridade;

}
