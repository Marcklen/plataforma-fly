package br.com.plataformafly.usuarioapi.model.dto.out;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsuarioUpdateDTO implements Serializable {

    private String nome;
    private String login;
    private String email;
    private Boolean admin;
    private String password;
}
