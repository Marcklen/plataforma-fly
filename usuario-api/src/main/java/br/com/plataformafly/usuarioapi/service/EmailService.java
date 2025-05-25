package br.com.plataformafly.usuarioapi.service;

import br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.EmailDTO;

import java.util.List;

public interface EmailService {

    void enviarEmailParaFila(EmailMessageDTO dto);
    EmailDTO buscarPorId(Integer id);
    void salvarEmail(String destinatario, String assunto, String corpo);
    List<EmailDTO> listarTodos();
}
