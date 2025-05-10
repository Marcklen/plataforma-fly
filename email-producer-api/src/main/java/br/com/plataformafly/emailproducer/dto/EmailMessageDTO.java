package br.com.plataformafly.emailproducer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO {

    private String remetente;
    private String destinatario;
    private String assunto;
    private String corpo;
}