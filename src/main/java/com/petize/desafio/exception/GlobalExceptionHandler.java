package com.petize.desafio.exception;

import com.petize.desafio.model.dto.error.ErrorRespondeDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // 404 - recurso não encontrado (ex.: tarefa/subtarefa)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorRespondeDto> handleNotFound(EntityNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 422 - violação de regra de negócio (ex.: concluir tarefa com subtarefas pendentes)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorRespondeDto> handleBusiness(IllegalStateException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // 400 - validação de @Valid no corpo (DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespondeDto> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return buildErrorResponse(msg, HttpStatus.BAD_REQUEST);
    }

    // 400 - validação de parâmetros (ex.: @RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorRespondeDto> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));
        return buildErrorResponse(msg, HttpStatus.BAD_REQUEST);
    }

    // 400 - JSON malformado/enum inválido/data inválida, etc.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespondeDto> handleNotReadable(HttpMessageNotReadableException ex) {
        return buildErrorResponse("Corpo da requisição inválido ou mal formatado.", HttpStatus.BAD_REQUEST);
    }

    // 405 - método HTTP não suportado
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorRespondeDto> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return buildErrorResponse("Método HTTP não suportado para este endpoint.", HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 409 - violação de integridade (ex.: unique constraint do título)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespondeDto> handleConflict(DataIntegrityViolationException ex) {
        return buildErrorResponse("Violação de integridade de dados.", HttpStatus.CONFLICT);
    }

    // 500 - fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespondeDto> handleAll(Exception ex) {
        return buildErrorResponse("Erro interno do servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorRespondeDto> buildErrorResponse(String message, HttpStatus status) {
        ErrorRespondeDto body = new ErrorRespondeDto(
                LocalDateTime.now().format(FORMATTER),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return ResponseEntity.status(status).body(body);
    }
}
