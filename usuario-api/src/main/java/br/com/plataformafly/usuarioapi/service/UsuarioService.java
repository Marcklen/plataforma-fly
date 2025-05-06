package br.com.plataformafly.usuarioapi.service;

import br.com.plataformafly.usuarioapi.model.Usuario;
import br.com.plataformafly.usuarioapi.model.dto.in.UsuarioDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioCreateDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioUpdateDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioDTO criar(UsuarioCreateDTO dto);
    List<UsuarioDTO> listar();
    UsuarioDTO atualizar(Integer id, UsuarioUpdateDTO dto);
    UsuarioDTO buscarPorId(Integer id);
    void deletar(Integer id);
    UsuarioDTO buscarPorLogin(String login);
}
