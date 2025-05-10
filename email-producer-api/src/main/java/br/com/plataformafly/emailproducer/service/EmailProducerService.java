package br.com.plataformafly.emailproducer.service;

import br.com.plataformafly.emailproducer.dto.EmailMessageDTO;

public interface EmailProducerService {

    void enviarParaFila(EmailMessageDTO dto);
}
