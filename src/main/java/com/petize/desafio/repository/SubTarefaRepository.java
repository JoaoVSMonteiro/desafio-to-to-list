package com.petize.desafio.repository;

import com.petize.desafio.model.entity.Subtarefa;
import com.petize.desafio.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubTarefaRepository extends JpaRepository<Subtarefa, Long> {

    boolean existsByTarefa_IdTarefaAndStatusNot(Long idTarefa, Status status);

    @Query("""
       select (count(s) > 0)
       from Subtarefa s
       where s.tarefa.idTarefa = :idTarefa
         and s.status <> :statusConcluida
    """)
    boolean existeNaoConcluida(@Param("idTarefa") Long idTarefa,@Param("statusConcluida") Status statusConcluida );

    List<Subtarefa> findByTarefa_IdTarefa(Long idTarefa);

}
