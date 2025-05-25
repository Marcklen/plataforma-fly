package br.com.plataformafly.usuarioapi.model.dto.out;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailDTO {

    private Integer id;
    private String destinatario;
    private String assunto;
    private String corpo;
    private boolean enviado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataEnvio;

}
