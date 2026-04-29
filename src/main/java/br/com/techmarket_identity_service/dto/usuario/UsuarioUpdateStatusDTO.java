package br.com.techmarket_identity_service.dto.usuario;

import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateStatusDTO(
        @NotNull(message = "Status é obrigatório") StatusUsuario status
) {
}
