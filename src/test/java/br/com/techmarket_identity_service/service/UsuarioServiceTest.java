package br.com.techmarket_identity_service.service;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioResponseDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateDTO;
import br.com.techmarket_identity_service.model.Perfil;
import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import br.com.techmarket_identity_service.repository.PerfilRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioResponseDTO responseDTO;
    private Perfil perfil;

    @BeforeEach
    void setup() {

        perfil = new Perfil();
        perfil.setId(1L);
        perfil.setTipoPerfil(TipoPerfil.ADMINISTRADOR);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setPerfil(perfil);

        responseDTO = new UsuarioResponseDTO(
                1L,
                "João Silva",
                "joao@email.com",
                "12345678901",
                StatusUsuario.ATIVO,
                TipoPerfil.ADMINISTRADOR
        );
    }

    @Test
    void deveRetornarUsuariosPaginados() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> usuariosPage = new PageImpl<>(List.of(usuario));

        when(usuarioRepository.findAll(pageable)).thenReturn(usuariosPage);

        Page<UsuarioResponseDTO> resultado = usuarioService.obterTodosUsuarios(pageable);

        assertEquals(1, resultado.getTotalElements());

        verify(usuarioRepository).findAll(pageable);
    }

    @Test
    void deveRetornarUsuarioPorId() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

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

        UsuarioCreateDTO createDTO = new UsuarioCreateDTO(
                "João Silva",
                "joao@email.com",
                "12345678901",
                "senha123",
                1L
        );

        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.cadastrarUsuario(createDTO);

        assertNotNull(resultado);

        verify(perfilRepository).findById(1L);
        verify(passwordEncoder).encode(anyString());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveAtualizarUsuario() {

        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO(
                "João Silva",
                "joao@email.com",
                "12345678901",
                "novaSenha123",
                StatusUsuario.ATIVO,
                1L
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        UsuarioResponseDTO resultado = usuarioService.atualizarUsuario(1L, updateDTO);

        assertNotNull(resultado);
        assertEquals(1L, usuario.getId());

        verify(usuarioRepository).save(usuario);
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void deveDeletarUsuario() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deletarUsuario(1L);

        verify(usuarioRepository).delete(usuario);
    }
}