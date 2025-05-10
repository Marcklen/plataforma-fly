package br.com.plataformafly.emailconsumer.service;

import br.com.plataformafly.emailconsumer.dto.EmailMessageDTO;

public interface EmailKafkaListenerService {

    void receberMensagem(EmailMessageDTO dto);
}
