package com.petize.desafio.exception;

import jakarta.persistence.EntityNotFoundException;

public class RecursoNaoEncontradoException extends EntityNotFoundException {
    public RecursoNaoEncontradoException(String recurso, Object id) {
        super(recurso + " com id = " + id + " não encontrada");
    }
}
