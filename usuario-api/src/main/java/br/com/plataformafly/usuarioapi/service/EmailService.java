package br.com.plataformafly.usuarioapi.service;

import br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO;

public interface EmailService {

    void enviarEmailParaFila(EmailMessageDTO dto);
}
