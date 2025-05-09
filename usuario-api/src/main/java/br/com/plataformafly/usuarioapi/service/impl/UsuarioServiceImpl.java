package br.com.plataformafly.usuarioapi.service.impl;

import br.com.plataformafly.usuarioapi.controller.exceptions.handler.AcessoNegadoCustomException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioExistenteException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioNaoEncontradoException;
import br.com.plataformafly.usuarioapi.model.Usuario;
import br.com.plataformafly.usuarioapi.model.dto.in.UsuarioDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioCreateDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioUpdateDTO;
import br.com.plataformafly.usuarioapi.model.repository.UsuarioRepository;
import br.com.plataformafly.usuarioapi.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper mapper;
    private final BCryptPasswordEncoder encoder;

    @Override
    public UsuarioDTO criar(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new UsuarioExistenteException("Login já existente.");
        }

        Usuario usuario = mapper.convertValue(dto, Usuario.class);
        usuario.setPassword(encoder.encode(dto.getPassword()));
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
    @CacheEvict(value = "usuarios", key = "#id") // remove/invalida o resultado do cache para o id do usuário
    public UsuarioDTO atualizar(Integer id, UsuarioUpdateDTO dto) {
        Usuario usuario = buscarUsuariosPorID(id);
        boolean isVoce = usuario.getLogin().equals(getCurrentUsername());
        boolean isAdmin = isAdmin(); // para nao chamar duas vezes o mesmo metodo

        // Validação para alteração da senha
        if (dto.getPassword() != null) {
            if (!isAdmin && !isVoce) {
                throw new AcessoNegadoCustomException("Somente administradores podem alterar senhas de outros usuários.");
            }
            usuario.setPassword(encoder.encode(dto.getPassword()));
        }

        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getAdmin() != null) {
            if (!isAdmin) {
                throw new AcessoNegadoCustomException("Somente administradores podem alterar a flag de administrador.");
            }
            usuario.setAdmin(dto.getAdmin());
        }

        usuario.setAtualizadoEm(LocalDateTime.now());
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return mapper.convertValue(usuarioAtualizado, UsuarioDTO.class);
    }


    @Override
    @Cacheable(value = "usuarios", key = "#id") // armazena o resultado em cache para o id do usuário
    public UsuarioDTO buscarPorId(Integer id) {
        Usuario usuario = buscarUsuariosPorID(id);
        return mapper.convertValue(usuario, UsuarioDTO.class);
    }

    @Override
    @CacheEvict(value = "usuarios", key = "#id")
    public void deletar(Integer id) {
        if (!isAdmin()) {
            throw new AcessoNegadoCustomException("Somente administradores podem excluir usuários!!!");
        }
        Usuario usuario = buscarUsuariosPorID(id);
        usuarioRepository.delete(usuario);
    }

    // função para buscar usuário por login utilizado no AuthService
    @Override
    //TODO @Cacheable(value = "usuarios", key = "#login") // armazena o resultado em cache para o login do usuário -->comentado pois esta dando erro / verificar esse erro
    public UsuarioDTO buscarPorLogin(String login) {
        var usuario = usuarioRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        return mapper.convertValue(usuario, UsuarioDTO.class);
    }

    private Usuario buscarUsuariosPorID(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado!!!"));
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
