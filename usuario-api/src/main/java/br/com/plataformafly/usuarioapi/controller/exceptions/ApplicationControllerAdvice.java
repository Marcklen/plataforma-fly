package br.com.plataformafly.usuarioapi.controller.exceptions;

import br.com.plataformafly.usuarioapi.controller.exceptions.handler.AcessoNegadoCustomException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.ApiErrors;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioExistenteException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

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

    @ExceptionHandler(AcessoNegadoCustomException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrors handleAcessoNegadoCustomException(AcessoNegadoCustomException ex) {
        return new ApiErrors(ex.getMessage());
    }

    /**
     * Handle Access Denied Exception e Authorization Denied Exception
     * @return ApiErrors com base no PreAuthorize para tratar os erros de acesso num formato similar aos demais
     */
    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrors handleAccessDeniedSpringSecurity(Exception ex) {
        return new ApiErrors("Acesso Negado: você não tem permissão para esta operação.");
    }
}
