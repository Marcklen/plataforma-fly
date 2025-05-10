package br.com.plataformafly.emailconsumer.service.impl;

import br.com.plataformafly.emailconsumer.dto.EmailMessageDTO;
import br.com.plataformafly.emailconsumer.service.EmailKafkaListenerService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailKafkaListenerServiceImpl implements EmailKafkaListenerService {

    private final JavaMailSender mailSender;

    @Override
    @KafkaListener(topics = "email-envio", groupId = "email-group", containerFactory = "kafkaListenerContainerFactory")
    public void receberMensagem(EmailMessageDTO dto) {
        log.info("Mensagem recebida via Kafka para: {}", dto.getDestinatario());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(dto.getRemetente());
            helper.setTo(dto.getDestinatario());
            helper.setSubject(dto.getAssunto());
            helper.setText(dto.getCorpo(), true); // true = HTML

            mailSender.send(message);
            log.info("E-mail enviado com sucesso para {}", dto.getDestinatario());

        } catch (MessagingException e) {
            log.error("Erro ao enviar e-mail para {}: {}", dto.getDestinatario(), e.getMessage());
        }
    }
}
