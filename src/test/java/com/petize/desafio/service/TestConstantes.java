package com.petize.desafio.service;

import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstantes {

    public static final Long ID_TAREFA_1 = 1L;
    public static final Long ID_TAREFA_2 = 2L;
    public static final Long ID_TAREFA_INEXISTENTE = 999L;

    public static final String TITULO_TAREFA_1 = "Planejar backlog";
    public static final String TITULO_TAREFA_2 = "Estudar Spring";
    public static final String TITULO_TAREFA_DUPLICADA = "Repetida";

    public static final String DESCRICAO_TAREFA_1 = "Organizar histórias do próximo sprint";
    public static final String DESCRICAO_TAREFA_2 = "Avançar no módulo de validação";

    public static final String DATA_VENCIMENTO_PADRAO_STR = "2025-08-20";
    public static final LocalDate DATA_VENCIMENTO_PADRAO = LocalDate.of(2025, 8, 20);
    public static final LocalDate DATA_VENCIMENTO_ALTERNATIVA = LocalDate.of(2025, 8, 21);

    public static final Status ST_PENDENTE = Status.PENDENTE;
    public static final Status ST_EM_ANDAMENTO = Status.EM_ANDAMENTO;
    public static final Status ST_CONCLUIDA = Status.CONCLUIDA;
    public static final Status ST_CANCELADA = Status.CANCELADA;


    public static final Prioridade PRIO_ALTA = Prioridade.ALTA;

    public static final String MSG_ERRO_MAPEAR = "erro de mapeamento";


    public static final Long ID_SUBTAREFA_1 = 10L;
    public static final Long ID_SUBTAREFA_INEXISTENTE = 888L;

    public static final String TITULO_SUB_1 = "Definir critérios";
    public static final String TITULO_SUB_2 = "Revisar documentação";


}

