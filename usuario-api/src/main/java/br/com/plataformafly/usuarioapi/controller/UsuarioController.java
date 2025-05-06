package br.com.plataformafly.usuarioapi.controller;

import br.com.plataformafly.usuarioapi.model.dto.in.UsuarioDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioCreateDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioUpdateDTO;
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

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioCreateDTO dto) {
        return new ResponseEntity<>(usuarioService.criar(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return new ResponseEntity<>(usuarioService.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Integer id) {
        return new ResponseEntity<>(usuarioService.buscarPorId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        return new ResponseEntity<>(usuarioService.atualizar(id, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        usuarioService.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UsuarioDTO> buscarPorLogin(@PathVariable String login) {
        return ResponseEntity.ok(usuarioService.buscarPorLogin(login));
    }
}
