package br.com.techmarket_identity_service.dto.usuario;

import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "E-mail no formato inválido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O cpf deve ter 11 caracteres")
    private String cpf;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 150, message = "A senha deve ter entre 6 e 100 caracteres")
    private String senha;

    @NotNull(message = "Status do usuário é obrigatório")
    private StatusUsuario status;
}
