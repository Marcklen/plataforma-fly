package br.com.plataformafly.usuarioapi.service.impl;

import br.com.plataformafly.usuarioapi.controller.exceptions.handler.AcessoNegadoCustomException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioExistenteException;
import br.com.plataformafly.usuarioapi.controller.exceptions.handler.UsuarioNaoEncontradoException;
import br.com.plataformafly.usuarioapi.model.Usuario;
import br.com.plataformafly.usuarioapi.model.dto.in.EmailMessageDTO;
import br.com.plataformafly.usuarioapi.model.dto.in.UsuarioDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioCreateDTO;
import br.com.plataformafly.usuarioapi.model.dto.out.UsuarioUpdateDTO;
import br.com.plataformafly.usuarioapi.model.repository.UsuarioRepository;
import br.com.plataformafly.usuarioapi.service.EmailService;
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
    private final EmailService emailService;

    @Override
    public UsuarioDTO criar(UsuarioCreateDTO dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new UsuarioExistenteException("Login já existente.");
        }

        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UsuarioExistenteException("E-mail já cadastrado.");
        }

        Usuario usuario = mapper.convertValue(dto, Usuario.class);
        usuario.setPassword(encoder.encode(dto.getPassword()));
        usuario.setCriadoEm(LocalDateTime.now());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        UsuarioDTO usuarioDTO = mapper.convertValue(usuarioSalvo, UsuarioDTO.class);

        if (Boolean.TRUE.equals(usuarioDTO.getAdmin())) {
            EmailMessageDTO email = new EmailMessageDTO();
            email.setRemetente("noreply@plataformafly.com");
            email.setDestinatario(usuarioDTO.getEmail());
            email.setAssunto("Cadastro realizado com sucesso!");
            email.setCorpo("Olá " + usuarioDTO.getNome() + ", sua conta de administrador foi criada com sucesso.");
            emailService.enviarEmailParaFila(email);
            emailService.salvarEmail(usuarioDTO.getEmail(), email.getAssunto(), email.getCorpo());
        }
        return usuarioDTO;
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
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if (!isAdmin && !isVoce) {
                throw new AcessoNegadoCustomException("Somente administradores podem alterar senhas de outros usuários.");
            }
            usuario.setPassword(encoder.encode(dto.getPassword()));
        }

        if (dto.getLogin() != null && !dto.getLogin().isEmpty() && !dto.getLogin().equals(usuario.getLogin())) {
            if (!isAdmin && !isVoce) {
                throw new AcessoNegadoCustomException("Somente administradores podem alterar logins de outros usuários.");
            }
            if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
                throw new UsuarioExistenteException("Login já existente.");
            }
            usuario.setLogin(dto.getLogin());
        }

        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            if (!dto.getEmail().equals(usuario.getEmail()) && usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new UsuarioExistenteException("E-mail já cadastrado.");
            }
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

    @Override
    public UsuarioDTO notificarUsuarioComum(UsuarioDTO usuarioDTO, String assunto, String corpo) {
        EmailMessageDTO email = new EmailMessageDTO();
        email.setRemetente("noreply@plataformafly.com");
        email.setDestinatario(usuarioDTO.getEmail());
        email.setAssunto(assunto);
        email.setCorpo(corpo);
        emailService.enviarEmailParaFila(email);
        emailService.salvarEmail(usuarioDTO.getEmail(), assunto, corpo);
        return usuarioDTO;
    }

    @Override
    public UsuarioDTO notificarUsuarioAdmin(String assunto, String corpo) {
        List<Usuario> admins = usuarioRepository.findAllByAdminIsTrue();
        if (admins.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Nenhum administrador encontrado.");
        }
        for (Usuario admin : admins) {
            EmailMessageDTO email = new EmailMessageDTO();
            email.setRemetente("noreply@plataformafly.com");
            email.setDestinatario(admin.getEmail());
            email.setAssunto(assunto);
            email.setCorpo(corpo);
            emailService.enviarEmailParaFila(email);
            emailService.salvarEmail(admin.getEmail(), assunto, corpo);
        }
        // Retorna o primeiro só como referência
        return mapper.convertValue(admins.get(0), UsuarioDTO.class);
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
