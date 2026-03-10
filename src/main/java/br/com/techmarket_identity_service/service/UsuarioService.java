package br.com.techmarket_identity_service.service;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioResponseDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateDTO;
import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import br.com.techmarket_identity_service.repository.PerfilRepository;
import br.com.techmarket_identity_service.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PerfilRepository perfilRepository;

    public Page<UsuarioResponseDTO> obterTodosUsuarios(Pageable paginacao) {
        return usuarioRepository
                .findAll(paginacao)
                .map(this::converterParaDTO);
    }

    public UsuarioResponseDTO obterUsuarioPorId(Long id) {
        Usuario usuario = buscarEntidadeUsuarioPorId(id);
        return converterParaDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(UsuarioCreateDTO dto) {
        var perfil = perfilRepository.findById(dto.getPerfilId())
                        .orElseThrow(() -> new EntityNotFoundException("Perfil com id: " + dto.getPerfilId() + " não encontrado"));

        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNome(dto.getNome());
        usuarioEntity.setEmail(dto.getEmail());
        usuarioEntity.setCpf(dto.getCpf());
        usuarioEntity.setSenha(dto.getSenha());
        usuarioEntity.setStatus(StatusUsuario.ATIVO);
        usuarioEntity.setPerfil(perfil);

        usuarioEntity = usuarioRepository.save(usuarioEntity);
        return converterParaDTO(usuarioEntity);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuarioEntity = buscarEntidadeUsuarioPorId(id);

        var perfil = perfilRepository.findById(dto.getPerfilId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Perfil com id: " + dto.getPerfilId() + " não encontrado"));

        usuarioEntity.setNome(dto.getNome());
        usuarioEntity.setEmail(dto.getEmail());
        usuarioEntity.setCpf(dto.getCpf());
        usuarioEntity.setSenha(dto.getSenha());
        usuarioEntity.setStatus(dto.getStatus());
        usuarioEntity.setPerfil(perfil);

        usuarioRepository.save(usuarioEntity);

        return converterParaDTO(usuarioEntity);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        var usuario = buscarEntidadeUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarEntidadeUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com id: " + id + " não encontrado"));
    }

    private UsuarioResponseDTO converterParaDTO(Usuario usuario) {
        UsuarioResponseDTO dto = modelMapper.map(usuario, UsuarioResponseDTO.class);
        dto.setTipoPerfil(usuario.getPerfil().getTipoPerfil());

        return dto;
    }
}
