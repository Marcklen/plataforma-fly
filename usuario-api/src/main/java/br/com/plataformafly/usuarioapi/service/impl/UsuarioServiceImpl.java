package br.com.plataformafly.usuarioapi.service.impl;

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
import org.springframework.security.access.AccessDeniedException;
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
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioDTO atualizar(Integer id, UsuarioUpdateDTO dto) {
        Usuario usuario = buscarUsuariosPorID(id);
        boolean isOwner = usuario.getLogin().equals(getCurrentUsername());
        boolean isAdmin = isAdmin();

        // Acesso negado se não for admin nem dono
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Você não tem permissão para atualizar este usuário.");
        }
        // Qualquer um com permissão pode alterar a própria senha
        if (dto.getPassword() != null) {
            usuario.setPassword(encoder.encode(dto.getPassword()));
        }
        // Apenas o próprio usuário pode alterar nome/email
        if (isOwner) {
            if (dto.getNome() != null) usuario.setNome(dto.getNome());
            if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
        }
        // Apenas admin pode mudar o campo admin
        if (dto.getAdmin() != null && isAdmin) {
            usuario.setAdmin(dto.getAdmin());
        }
        usuario.setAtualizadoEm(LocalDateTime.now());
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        System.out.println("Usuário autenticado: " + getCurrentUsername());
        System.out.println("É ADMIN? " + isAdmin());
        System.out.println("Atualizado: " + usuarioAtualizado);
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

    @Override
    @Cacheable(value = "usuarios", key = "#login")
    public UsuarioDTO buscarPorLogin(String login) {
        var usuario = usuarioRepository
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setLogin(usuario.getLogin());
        dto.setPassword(usuario.getPassword());
        dto.setEmail(usuario.getEmail());
        dto.setAdmin(usuario.getAdmin());
        dto.setCriadoEm(usuario.getCriadoEm());
        dto.setAtualizadoEm(usuario.getAtualizadoEm());
        return dto;
    }

    private Usuario buscarUsuariosPorID(Integer id) {
        return usuarioRepository
                .findById(id)
                .orElseThrow(
                        () -> new UsuarioNaoEncontradoException("Usuario não encontrado!!!")
                );
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
