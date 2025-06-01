package br.com.plataformafly.authapi.controller.exceptions;

import br.com.plataformafly.authapi.controller.exceptions.handler.ApiErrors;
import br.com.plataformafly.authapi.controller.exceptions.handler.CredenciaisInvalidasException;
import br.com.plataformafly.authapi.controller.exceptions.handler.UsuarioNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(CredenciaisInvalidasException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleUsuarioExistenteException(CredenciaisInvalidasException ex) {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        return new ApiErrors(ex.getMessage());
    }
}
