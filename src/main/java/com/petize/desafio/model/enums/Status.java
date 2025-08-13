package com.petize.desafio.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Status da tarefa/subtarefa")
public enum Status {
    PENDENTE, EM_ANDAMENTO, CONCLUIDA, CANCELADA
}
