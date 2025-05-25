package br.com.plataformafly.usuarioapi.model.repository;

import br.com.plataformafly.usuarioapi.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Integer> {
}
