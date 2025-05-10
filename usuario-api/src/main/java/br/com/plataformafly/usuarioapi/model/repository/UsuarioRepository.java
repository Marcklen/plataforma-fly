package br.com.plataformafly.usuarioapi.model.repository;

import br.com.plataformafly.usuarioapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByLogin(String login);
    List<Usuario> findAllByAdminIsTrue();
}
