package br.com.techmarket_identity_service.dto.usuario;

import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id_usuario", "nome", "email", "cpf", "status", "tipo_perfil"})
public record UsuarioResponseDTO (
        @JsonProperty("id_usuario")
        Long id,
        String nome,
        String email,
        String cpf,
        StatusUsuario status,
        @JsonProperty("tipo_perfil")
        TipoPerfil tipoPerfil
){}
