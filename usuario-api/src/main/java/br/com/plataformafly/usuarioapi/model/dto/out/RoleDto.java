package br.com.plataformafly.usuarioapi.model.dto.out;

import br.com.plataformafly.usuarioapi.model.dto.enums.TipoRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Integer id;
    private TipoRoles nome;
}