package br.com.plataformafly.authapi.controller;

import br.com.plataformafly.authapi.model.dto.in.LoginRequestDTO;
import br.com.plataformafly.authapi.model.dto.out.LoginResponseDTO;
import br.com.plataformafly.authapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/autenticacao")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDto) {
        return ResponseEntity.ok(authService.authenticate(loginDto));
    }
}
