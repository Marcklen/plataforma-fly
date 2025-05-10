package br.com.plataformafly.usuarioapi.service.impl;

import br.com.plataformafly.usuarioapi.config.RabbitMQConfig;
import br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO;
import br.com.plataformafly.usuarioapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void enviarEmailParaFila(EmailMessageDTO dto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_EMAIL, RabbitMQConfig.ROUTING_KEY_EMAIL, dto);
        log.info("Email enviado com sucesso para a fila: {}", dto);
    }
}
