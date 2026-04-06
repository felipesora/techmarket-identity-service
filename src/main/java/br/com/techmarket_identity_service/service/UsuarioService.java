package br.com.techmarket_identity_service.service;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioResponseDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateSenhaDTO;
import br.com.techmarket_identity_service.exception.SenhaAtualIncorretaException;
import br.com.techmarket_identity_service.mapper.UsuarioMapper;
import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.repository.PerfilRepository;
import br.com.techmarket_identity_service.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerfilRepository perfilRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.perfilRepository = perfilRepository;
    }

    public Page<UsuarioResponseDTO> obterTodosUsuarios(Pageable paginacao) {
        return usuarioRepository
                .findAll(paginacao)
                .map(UsuarioMapper::converterParaResponseDTO);
    }

    public UsuarioResponseDTO obterUsuarioPorId(Long id) {
        Usuario usuario = buscarEntidadeUsuarioPorId(id);
        return UsuarioMapper.converterParaResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(UsuarioCreateDTO dto) {
        var perfil = perfilRepository.findById(dto.perfilId())
                        .orElseThrow(() -> new EntityNotFoundException("Perfil com id: " + dto.perfilId() + " não encontrado"));

        Usuario usuarioEntity = UsuarioMapper.converterCreateDTOParaEntity(dto, perfil);
        usuarioEntity.setSenha(passwordEncoder.encode(dto.senha()));

        usuarioRepository.save(usuarioEntity);
        return UsuarioMapper.converterParaResponseDTO(usuarioEntity);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuarioEntity = buscarEntidadeUsuarioPorId(id);

        var perfil = perfilRepository.findById(dto.perfilId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Perfil com id: " + dto.perfilId() + " não encontrado"));

        UsuarioMapper.converterUpdateDTOParaEntity(dto, usuarioEntity, perfil);
        usuarioRepository.save(usuarioEntity);

        return UsuarioMapper.converterParaResponseDTO(usuarioEntity);
    }

    @Transactional
    public void atualizarSenha(Long id, UsuarioUpdateSenhaDTO dto) {
        Usuario usuario = buscarEntidadeUsuarioPorId(id);

        boolean senhaValida = passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha());

        if (!senhaValida) {
            throw new SenhaAtualIncorretaException();
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
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
}
