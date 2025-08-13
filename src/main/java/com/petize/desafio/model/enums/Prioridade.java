package com.petize.desafio.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Prioridade da tarefa")
public enum Prioridade {
    BAIXA, MEDIA, ALTA
}
