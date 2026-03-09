package br.com.techmarket_identity_service.service;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioResponseDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateDTO;
import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.model.enums.StatusUsuario;
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

    public Page<UsuarioResponseDTO> obterTodosUsuarios(Pageable paginacao) {
        return usuarioRepository
                .findAll(paginacao)
                .map(u -> modelMapper.map(u, UsuarioResponseDTO.class));
    }

    public UsuarioResponseDTO obterUsuarioPorId(Long id) {
        Usuario usuario = buscarEntidadeUsuarioPorId(id);
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(UsuarioCreateDTO dto) {
        Usuario usuarioEntity = modelMapper.map(dto, Usuario.class);
        usuarioEntity.setStatus(StatusUsuario.ATIVO);
        usuarioRepository.save(usuarioEntity);

        return modelMapper.map(usuarioEntity, UsuarioResponseDTO.class);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuarioEntity = modelMapper.map(dto, Usuario.class);
        usuarioEntity.setId(id);
        usuarioEntity = usuarioRepository.save(usuarioEntity);
        return modelMapper.map(usuarioEntity, UsuarioResponseDTO.class);
    }

    @Transactional
    public void deletar(Long id) {
        var usuario = buscarEntidadeUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarEntidadeUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com id: " + id + " não encontrado"));
    }
}
