package br.com.plataformafly.usuarioapi.model.dto.out;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    private String nome;
    private String email;
    private Boolean admin;
    private String password;
}
