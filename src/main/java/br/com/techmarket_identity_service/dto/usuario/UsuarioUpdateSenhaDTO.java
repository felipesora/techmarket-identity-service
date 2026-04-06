package br.com.techmarket_identity_service.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateSenhaDTO(
        @NotBlank(message = "Senha atual é obrigatória")
        @JsonProperty("senha_atual")
        String senhaAtual,

        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 6, max = 150, message = "A senha deve ter entre 6 e 150 caracteres")
        @JsonProperty("nova_senha")
        String novaSenha
) {
}
