package br.com.plataformafly.usuarioapi.model.repository;

import br.com.plataformafly.usuarioapi.model.Role;
import br.com.plataformafly.usuarioapi.model.dto.enums.TipoRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByNome(TipoRoles nome);
}