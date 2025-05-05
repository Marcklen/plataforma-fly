package br.com.plataformafly.usuarioapi.controller.exceptions.handler;

public class UsuarioExistenteException extends RuntimeException {

    public UsuarioExistenteException(String message) {
        super(message);
    }

    public UsuarioExistenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsuarioExistenteException(Throwable cause) {
        super(cause);
    }

    public UsuarioExistenteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
