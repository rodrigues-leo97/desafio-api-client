package com.devsuperior.desafio.resources.exceptions;

import com.devsuperior.desafio.services.exceptions.ExceptionBadRequest;
import com.devsuperior.desafio.services.exceptions.ExceptionEntityNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ExceptionEntityNotFound.class)
    public ResponseEntity<StandardError> entityNotFound(ExceptionEntityNotFound e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value()); //404
        err.setError("Resource not found");
        err.setMessage(e.getMessage()); //pegando a mensagem passada no método findById para quando o erro estourar
        err.setPath(request.getRequestURI()); //pega o caminho da requisição feita. EX: "/categories/6"

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ExceptionBadRequest.class)
    public ResponseEntity<StandardError> badRequest(ExceptionBadRequest e, HttpServletRequest request) {
        StandardError err = new StandardError();
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value()); //400
        err.setError("Resource not found");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }
}
