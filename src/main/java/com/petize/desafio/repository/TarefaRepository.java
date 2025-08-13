package com.petize.desafio.repository;

import com.petize.desafio.model.entity.Tarefa;
import com.petize.desafio.model.enums.Prioridade;
import com.petize.desafio.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    //filtros sem paginação
    @Query("""
    SELECT t FROM Tarefa t
    WHERE (:status IS NULL OR t.status = :status)
        AND (:prioridade IS NULL OR t.prioridade = :prioridade)
        AND (:dataVencimento IS NULL OR t.dataVencimento = :dataVencimento)
    ORDER BY t.dataVencimento ASC, t.idTarefa ASC
""")
    List<Tarefa> findByFiltros(@Param("status") Status status,
                               @Param("prioridade") Prioridade prioridade,
                               @Param("dataVencimento") LocalDate dataVencimento);


    //paginação
    @Query("""
    SELECT t FROM Tarefa t
    WHERE (:idTarefa IS NULL OR t.idTarefa = :idTarefa)
      AND (:status IS NULL OR t.status = :status)
      AND (:prioridade IS NULL OR t.prioridade = :prioridade)
      AND (:dataVencimento IS NULL OR t.dataVencimento = :dataVencimento)
""")
    Page<Tarefa> buscarPorFiltrosComId(@Param("idTarefa") Long idTarefa,
                                       @Param("status") Status status,
                                       @Param("prioridade") Prioridade prioridade,
                                       @Param("dataVencimento") LocalDate dataVencimento,
                                       Pageable pageable);

    boolean existsByTituloTarefaIgnoreCase(String titulo);
}
