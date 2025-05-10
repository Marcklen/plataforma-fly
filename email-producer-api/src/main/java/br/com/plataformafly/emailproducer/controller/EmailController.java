package br.com.plataformafly.emailproducer.controller;

import br.com.plataformafly.emailproducer.dto.EmailMessageDTO;
import br.com.plataformafly.emailproducer.service.EmailProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailProducerService emailProducerService;

    @PostMapping
    public ResponseEntity<Void> enviarEmail(@RequestBody EmailMessageDTO dto) {
        emailProducerService.enviarParaFila(dto);
        return ResponseEntity.accepted().build();
//        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
