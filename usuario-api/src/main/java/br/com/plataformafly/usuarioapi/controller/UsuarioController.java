package br.com.plataformafly.usuarioapi.controller;

import br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO;
import br.com.plataformafly.usuarioapi.model.dto.in.UsuarioDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.EmailDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioCreateDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioUpdateDTO;
import br.com.plataformafly.usuarioapi.service.EmailService;
import br.com.plataformafly.usuarioapi.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioCreateDTO dto) {
        return new ResponseEntity<>(usuarioService.criar(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return new ResponseEntity<>(usuarioService.listar(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(usuarioService.buscarPorId(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        return new ResponseEntity<>(usuarioService.atualizar(id, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioDTO> buscarPorLogin(@PathVariable String login) {
        return ResponseEntity.ok(usuarioService.buscarPorLogin(login));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notificar/{id}")
    public ResponseEntity<UsuarioDTO> notificarUsuarioComum(@PathVariable Integer id, @RequestBody EmailMessageDTO mensagem) {
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        UsuarioDTO enviado = usuarioService.notificarUsuarioComum(usuario, mensagem.getAssunto(), mensagem.getCorpo());
        return new ResponseEntity<>(enviado, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notificar/admins")
    public ResponseEntity<UsuarioDTO> notificarTodosAdmins(@RequestBody EmailMessageDTO mensagem) {
        UsuarioDTO enviado = usuarioService.notificarUsuarioAdmin(mensagem.getAssunto(), mensagem.getCorpo());
        return new ResponseEntity<>(enviado, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email")
    public ResponseEntity<List<EmailDTO>> listarTodos() {
        return ResponseEntity.ok(emailService.listarTodos());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/email/{id}")
    public ResponseEntity<EmailDTO> buscarEmailPorId(@PathVariable Integer id) {
        EmailDTO email = emailService.buscarPorId(id);
        return ResponseEntity.ok(email);
    }

}
