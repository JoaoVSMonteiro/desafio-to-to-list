package com.petize.desafio.model.entity;

import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TAREFA")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tarefa")
    @SequenceGenerator(name = "seq_tarefa", sequenceName = "seq_tarefa", allocationSize = 1)
    @Column(name = "ID_TAREFA")
    private Long idTarefa;

    @Column(name = "TITULO_TAREFA", nullable = false, length = 150)
    private String tituloTarefa;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "DATA_VENCIMENTO", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORIDADE", nullable = false, length = 20)
    private Prioridade prioridade;

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL)
    private Set<Subtarefa> subtarefas;
            
}
