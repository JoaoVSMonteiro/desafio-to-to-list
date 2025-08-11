package com.petize.desafio.repository;

import com.petize.desafio.model.entity.Subtarefa;
import com.petize.desafio.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTarefaRepository extends JpaRepository<Subtarefa, Long> {

    boolean existsByTarefa_IdTarefaAndStatusNot(Long idTarefa, Status status);
}
