package br.com.techmarket_identity_service.dto.usuario;

import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id_usuario", "nome", "email", "cpf", "senha", "status"})
public class UsuarioResponseDTO {

    @JsonProperty("id_usuario")
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private StatusUsuario status;
}
