package br.com.plataformafly.usuarioapi.model.dto.out;

import lombok.Data;

import java.util.Set;

@Data
public class UsuarioUpdateDTO {

    private String nome;
    private String email;
    private String password;
    private Set<String> roles;
}
