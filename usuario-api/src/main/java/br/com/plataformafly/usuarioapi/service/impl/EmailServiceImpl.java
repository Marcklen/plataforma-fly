package br.com.plataformafly.usuarioapi.service.impl;

import br.com.plataformafly.usuarioapi.config.RabbitMQConfig;
import br.com.plataformafly.usuarioapi.model.Email;
import br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.EmailDTO;
import br.com.plataformafly.usuarioapi.model.repository.EmailRepository;
import br.com.plataformafly.usuarioapi.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final RabbitTemplate rabbitTemplate;
    private final EmailRepository emailRepository;
    private final ObjectMapper mapper;

    @Override
    public void enviarEmailParaFila(EmailMessageDTO dto) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_EMAIL, RabbitMQConfig.ROUTING_KEY_EMAIL, dto);
        log.info("Email enviado com sucesso para a fila: {}", dto);
    }

    @Override
    public EmailDTO buscarPorId(Integer id) {
        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado com ID: " + id));
        return mapper.convertValue(email, EmailDTO.class);
    }

    @Override
    public void salvarEmail(String destinatario, String assunto, String corpo) {
        Email email = Email.builder()
                .destinatario(destinatario)
                .assunto(assunto)
                .corpo(corpo)
                .enviado(true) // ou false, se quiser mudar baseado no retorno do envio real
                .dataCriacao(LocalDateTime.now())
                .dataEnvio(LocalDateTime.now())
                .build();
        emailRepository.save(email);
    }

    @Override
    public List<EmailDTO> listarTodos() {
        List<Email> emails = emailRepository.findAll(Sort.by(Sort.Direction.DESC, "dataCriacao"));
        return emails.stream().map(email -> mapper.convertValue(email, EmailDTO.class)).toList();
    }
}
