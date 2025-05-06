package br.com.plataformafly.usuarioapi.model.dto.enums;

import lombok.Getter;

@Getter
public enum TipoRoles {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String nome;

    TipoRoles(String nome) {
        this.nome = nome;
    }
}