package br.com.plataformafly.usuarioapi.controller.exceptions;

import br.com.plataformafly.usuarioapi.controller.exceptions.handler.ApiErrors;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioExistenteException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ApplicationControllerAdvice.class);

    @ExceptionHandler(UsuarioExistenteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleUsuarioExistenteException(UsuarioExistenteException ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Acesso negado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
