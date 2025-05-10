package br.com.plataformafly.emailconsumer.listener;

import br.com.plataformafly.emailconsumer.config.rabbit.RabbitMQConfig;
import br.com.plataformafly.emailconsumer.dto.EmailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQEmailListener {

    private final KafkaTemplate<String, EmailMessageDTO> kafkaTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void consumirFila(EmailMessageDTO dto) {
        log.info("Mensagem recebida via RabbitMQ: {}", dto);
        kafkaTemplate.send("email-envio", dto);
        log.info("Mensagem encaminhada para Kafka.");
    }
}