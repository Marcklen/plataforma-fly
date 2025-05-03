package br.com.plataformafly.usuarioapi.service.impl;

import br.com.plataformafly.usuarioapi.model.Usuario;
import br.com.plataformafly.usuarioapi.model.dto.in.UsuarioDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioCreateDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioUpdateDTO;
import br.com.plataformafly.usuarioapi.model.repository.UsuarioRepository;
import br.com.plataformafly.usuarioapi.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper mapper;

    @Override
    public UsuarioDTO criar(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new RuntimeException("Login já existente.");
        }

        Usuario usuario = mapper.convertValue(dto, Usuario.class);
        usuario.setCriadoEm(LocalDateTime.now());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return mapper.convertValue(usuarioSalvo, UsuarioDTO.class);
    }

    @Override
    public List<UsuarioDTO> listar() {
        return usuarioRepository
                .findAll()
                .stream()
                .map(u -> mapper.convertValue(u, UsuarioDTO.class))
                .toList();
    }

    @Override
    public UsuarioDTO atualizar(Integer id, UsuarioUpdateDTO dto) {
        Usuario usuario = buscarUsuariosPorID(id);

        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            usuario.setLogin(dto.getEmail());
        }
        if (dto.getAdmin() != null) {
            usuario.setAdmin(dto.getAdmin());
        }
        usuario.setAtualizadoEm(LocalDateTime.now());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return mapper.convertValue(usuarioAtualizado, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO buscarPorId(Integer id) {
        Usuario usuario = buscarUsuariosPorID(id);
        return mapper.convertValue(usuario, UsuarioDTO.class);
    }

    @Override
    public void deletar(Integer id) {
        Usuario usuario = buscarUsuariosPorID(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarUsuariosPorID(Integer id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Usuario não encontrado")
                );
    }
}
