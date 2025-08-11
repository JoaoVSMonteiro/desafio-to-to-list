package com.petize.desafio.model.entity;

import com.petize.desafio.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SUB_TAREFA")
public class Subtarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tarefa")
    @SequenceGenerator(name = "seq_tarefa", sequenceName = "seq_tarefa", allocationSize = 1)
    @Column(name = "ID_SUB_TAREFA")
    private Long idSubTarefa;

    @Column(name = "TITULO_SUB_TAREFA", nullable = false, length = 150)
    private String tituloSubTarefa;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "ID_TAREFA", nullable = false)
    private Tarefa tarefa;
}
