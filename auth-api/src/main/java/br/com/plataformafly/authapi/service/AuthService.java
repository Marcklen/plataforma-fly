package br.com.plataformafly.authapi.service;

import br.com.plataformafly.authapi.model.dto.in.LoginRequestDTO;
import br.com.plataformafly.authapi.model.dto.out.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO authenticate(LoginRequestDTO dto);
}
