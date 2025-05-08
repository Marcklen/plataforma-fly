package br.com.plataformafly.usuarioapi.service.impl;

import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioExistenteException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioNaoEncontradoException;
import br.com.plataformafly.usuarioapi.model.Role;
import br.com.plataformafly.usuarioapi.model.Usuario;
import br.com.plataformafly.usuarioapi.model.dto.enums.TipoRoles;
import br.com.plataformafly.usuarioapi.model.dto.in.UsuarioDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.RoleDto;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioCreateDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioUpdateDTO;
import br.com.plataformafly.usuarioapi.model.repository.RoleRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepository roleRepository;

    @Override
    public UsuarioDTO criar(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new UsuarioExistenteException("Login já existente.");
        }

        Set<Role> roles = mapearRoles(dto.getRoles());
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .login(dto.getLogin())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .roles(roles)
                .criadoEm(LocalDateTime.now())
                .build();
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

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Apenas o próprio usuário ou um administrador pode atualizar este cadastro.");
        }

        // Apenas o próprio usuário pode alterar nome/email
        if (isOwner) {
            if (dto.getNome() != null) usuario.setNome(dto.getNome());
            if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
        }
        // Apenas admin pode alterar roles
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            if (!isAdmin) {
                throw new AccessDeniedException("Somente administradores podem alterar os papéis de um usuário.");
            }
            Set<Role> novasRoles = mapearRoles(dto.getRoles());
            usuario.setRoles(novasRoles);
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
        dto.setRoles(usuario.getRoles()
                .stream()
                .map(role -> new RoleDto(role.getId(), role.getNome()))
                .collect(Collectors.toSet()));
        dto.setCriadoEm(usuario.getCriadoEm());
        dto.setAtualizadoEm(usuario.getAtualizadoEm());
//        Set<RoleDto> rolesDTO = usuario.getRoles().stream()
//                .map(role -> new RoleDto(role.getId(), role.getNome()))
//                .collect(Collectors.toSet());
//        dto.setRoles(rolesDTO);
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

    private Set<Role> mapearRoles(Set<String> roleNomes) {
        if (roleNomes == null || roleNomes.isEmpty()) {
            // Default para ROLE_USER se nenhum papel for enviado
            return Set.of(roleRepository.findByNome(TipoRoles.ROLE_USER)
                    .orElseThrow(() -> new IllegalArgumentException("Papel padrão ROLE_USER não encontrado.")));
        }
        return roleNomes.stream().map(nome -> {
            TipoRoles tipo;
            try {
                tipo = TipoRoles.valueOf(nome);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Papel inválido: " + nome);
            }
            return roleRepository.findByNome(tipo)
                    .orElseThrow(() -> new IllegalArgumentException("Role não encontrada no banco: " + nome));
        }).collect(Collectors.toSet());
    }

}
