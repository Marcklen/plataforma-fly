package br.com.plataformafly.usuarioapi.model.dto.out;

import br.com.plataformafly.usuarioapi.model.dto.enums.TipoRoles;
import lombok.Data;

@Data
public class RoleDto {
    private Integer id;
    private TipoRoles nome;
}