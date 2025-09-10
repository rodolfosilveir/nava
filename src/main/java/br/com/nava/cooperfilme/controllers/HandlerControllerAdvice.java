package br.com.nava.cooperfilme.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.nava.cooperfilme.api.responses.DefaultResponse;
import br.com.nava.cooperfilme.exceptions.ForbiddenVoteException;
import br.com.nava.cooperfilme.exceptions.NotFoundException;
import br.com.nava.cooperfilme.exceptions.ScriptAlreadyExistsException;
import br.com.nava.cooperfilme.exceptions.WithoutReasonException;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestControllerAdvice
public class HandlerControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> erros = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            erros.add(String.format("%s: %s; ", fieldName, errorMessage));
        });

        DefaultResponse<Void> response = DefaultResponse.<Void>builder()
            .httpStatus(400)
            .errors(erros)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    
    @ExceptionHandler(ScriptAlreadyExistsException.class)
    public ResponseEntity<DefaultResponse<Void>> handleScriptAlreadyExistsException(ScriptAlreadyExistsException ex) {

        DefaultResponse<Void> response = DefaultResponse.<Void>builder()
            .httpStatus(400)
            .errors(List.of(ex.getMessage()))
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    
    @ExceptionHandler(WithoutReasonException.class)
    public ResponseEntity<DefaultResponse<Void>> handleWithoutReasonException(WithoutReasonException ex) {

        DefaultResponse<Void> response = DefaultResponse.<Void>builder()
            .httpStatus(400)
            .errors(List.of(ex.getMessage()))
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DefaultResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {

        DefaultResponse<Void> response = DefaultResponse.<Void>builder()
            .httpStatus(403)
            .errors(List.of(ex.getMessage()))
            .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ForbiddenVoteException.class)
    public ResponseEntity<DefaultResponse<Void>> handleForbiddenVoteException(ForbiddenVoteException ex) {

        DefaultResponse<Void> response = DefaultResponse.<Void>builder()
            .httpStatus(403)
            .errors(List.of(ex.getMessage()))
            .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultResponse<Void>> handleNotFoundException(NotFoundException ex) {

        DefaultResponse<Void> response = DefaultResponse.<Void>builder()
            .httpStatus(404)
            .errors(List.of(ex.getMessage()))
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse<Void>> handleException(Exception ex) {

        DefaultResponse<Void> response = DefaultResponse.<Void>builder()
            .httpStatus(500)
            .errors(List.of(ex.getMessage()))
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
