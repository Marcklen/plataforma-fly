package br.com.plataformafly.emailproducer.service.impl;

import br.com.plataformafly.emailproducer.config.RabbitMQConfig;
import br.com.plataformafly.emailproducer.dto.EmailMessageDTO;
import br.com.plataformafly.emailproducer.service.EmailProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducerServiceImpl implements EmailProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void enviarParaFila(EmailMessageDTO dto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_EMAIL, RabbitMQConfig.ROUTING_KEY_EMAIL, dto);
        log.info("Mensagem publicada no RabbitMQ para o destinatário {}", dto.getDestinatario());
    }
}
