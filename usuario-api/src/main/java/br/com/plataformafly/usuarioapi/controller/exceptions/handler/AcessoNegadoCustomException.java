package br.com.plataformafly.usuarioapi.controller.exceptions.handler;

public class AcessoNegadoCustomException extends RuntimeException {

    public AcessoNegadoCustomException(String message) {
        super(message);
    }

    public AcessoNegadoCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcessoNegadoCustomException(Throwable cause) {
        super(cause);
    }

    public AcessoNegadoCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
