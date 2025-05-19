package br.com.plataformafly.authapi.controller.exceptions.handler;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String message) { super(message);}
}