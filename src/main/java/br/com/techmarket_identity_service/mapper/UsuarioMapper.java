package br.com.techmarket_identity_service.mapper;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioResponseDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateDTO;
import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.model.enums.StatusUsuario;

public final class UsuarioMapper {

    private UsuarioMapper () {}

    public static Usuario converterCreateDTOParaEntity(UsuarioCreateDTO dto) {

        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNome(dto.nome());
        usuarioEntity.setEmail(dto.email());
        usuarioEntity.setCpf(dto.cpf());
        usuarioEntity.setStatus(StatusUsuario.ATIVO);
        usuarioEntity.setPerfil(dto.perfil());

        return usuarioEntity;
    }

    public static Usuario converterUpdateDTOParaEntity(UsuarioUpdateDTO dto, Usuario usuario) {

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setCpf(dto.cpf());
        usuario.setStatus(dto.status());

        return usuario;
    }

    public static UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getStatus(),
                usuario.getPerfil()
        );
    }
}
