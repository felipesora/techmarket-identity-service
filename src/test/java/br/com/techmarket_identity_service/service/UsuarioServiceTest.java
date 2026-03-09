package br.com.techmarket_identity_service.service;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioResponseDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateDTO;
import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import br.com.techmarket_identity_service.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);

        responseDTO = new UsuarioResponseDTO();
    }

    @Test
    void deveRetornarUsuariosPaginados() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> usuariosPage = new PageImpl<>(List.of(usuario));

        when(usuarioRepository.findAll(pageable)).thenReturn(usuariosPage);
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(responseDTO);

        Page<UsuarioResponseDTO> resultado = usuarioService.obterTodosUsuarios(pageable);

        assertEquals(1, resultado.getTotalElements());
        verify(usuarioRepository).findAll(pageable);
    }

    @Test
    void deveRetornarUsuarioPorId() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(responseDTO);

        UsuarioResponseDTO resultado = usuarioService.obterUsuarioPorId(1L);

        assertNotNull(resultado);
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> usuarioService.obterUsuarioPorId(1L));
    }

    @Test
    void deveCadastrarUsuario() {

        UsuarioCreateDTO createDTO = new UsuarioCreateDTO();

        when(modelMapper.map(createDTO, Usuario.class)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(responseDTO);

        UsuarioResponseDTO resultado = usuarioService.cadastrarUsuario(createDTO);

        assertNotNull(resultado);
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveAtualizarUsuario() {

        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO();

        when(modelMapper.map(updateDTO, Usuario.class)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(responseDTO);

        UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(1L, updateDTO);

        assertNotNull(resultado);
        assertEquals(1L, usuario.getId());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveDeletarUsuario() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deletarUsuario(1L);

        verify(usuarioRepository).delete(usuario);
    }
}