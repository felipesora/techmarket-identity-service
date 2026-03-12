package br.com.techmarket_identity_service.controller;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioResponseDTO;
import br.com.techmarket_identity_service.dto.usuario.UsuarioUpdateDTO;
import br.com.techmarket_identity_service.exception.GlobalExceptionHandler;
import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import br.com.techmarket_identity_service.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UsuarioResponseDTO usuarioResponseDTO;
    private UsuarioCreateDTO usuarioCreateDTO;
    private UsuarioUpdateDTO usuarioUpdateDTO;

    @BeforeEach
    void setUp() {
        PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();

        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

        mockMvc = MockMvcBuilders
                .standaloneSetup(usuarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build();

        objectMapper = new ObjectMapper();

        // Setup DTOs de exemplo
        usuarioResponseDTO = new UsuarioResponseDTO(
                1L,
                "João Silva",
                "joao@email.com",
                "12345678901",
                StatusUsuario.ATIVO,
                TipoPerfil.ADMINISTRADOR
        );

        usuarioCreateDTO = new UsuarioCreateDTO(
                "João Silva",
                "joao@email.com",
                "12345678901",
                "senha123",
                1L
        );

        usuarioUpdateDTO = new UsuarioUpdateDTO(
                "João Silva Atualizado",
                "joao.atualizado@email.com",
                "98765432101",
                "senha456",
                StatusUsuario.ATIVO,
                2L
        );
    }

    @Test
    @DisplayName("Deve listar todos os usuários com paginação")
    void listarTodosUsuarios_DeveRetornarListaPaginada() throws Exception {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<UsuarioResponseDTO> usuariosList = Arrays.asList(usuarioResponseDTO);
        Page<UsuarioResponseDTO> page = new PageImpl<>(usuariosList, pageable, 1);

        when(usuarioService.obterTodosUsuarios(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/usuarios")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id_usuario", is(1)))
                .andExpect(jsonPath("$.content[0].nome", is("João Silva")))
                .andExpect(jsonPath("$.content[0].email", is("joao@email.com")))
                .andExpect(jsonPath("$.content[0].cpf", is("12345678901")))
                .andExpect(jsonPath("$.content[0].status", is("ATIVO")))
                .andExpect(jsonPath("$.content[0].tipo_perfil", is("ADMINISTRADOR")));

        verify(usuarioService, times(1)).obterTodosUsuarios(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void buscarPorId_DeveRetornarUsuario_QuandoIdExistir() throws Exception {
        // Arrange
        when(usuarioService.obterUsuarioPorId(1L)).thenReturn(usuarioResponseDTO);

        // Act & Assert
        mockMvc.perform(get("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario", is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva")))
                .andExpect(jsonPath("$.email", is("joao@email.com")))
                .andExpect(jsonPath("$.cpf", is("12345678901")))
                .andExpect(jsonPath("$.status", is("ATIVO")))
                .andExpect(jsonPath("$.tipo_perfil", is("ADMINISTRADOR")));

        verify(usuarioService, times(1)).obterUsuarioPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar usuário por ID inexistente")
    void buscarPorId_DeveRetornar404_QuandoIdNaoExistir() throws Exception {
        // Arrange
        when(usuarioService.obterUsuarioPorId(999L))
                .thenThrow(new EntityNotFoundException("Usuário não encontrado com ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Usuário não encontrado com ID: 999")));

        verify(usuarioService, times(1)).obterUsuarioPorId(999L);
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void cadastrarUsuario_DeveCriarUsuario_QuandoDadosValidos() throws Exception {
        // Arrange
        when(usuarioService.cadastrarUsuario(any(UsuarioCreateDTO.class))).thenReturn(usuarioResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/usuarios/1")))
                .andExpect(jsonPath("$.id_usuario", is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva")))
                .andExpect(jsonPath("$.email", is("joao@email.com")));

        verify(usuarioService, times(1)).cadastrarUsuario(any(UsuarioCreateDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 400 ao cadastrar usuário com dados inválidos")
    void cadastrarUsuario_DeveRetornar400_QuandoDadosInvalidos() throws Exception {
        // Arrange
        usuarioCreateDTO = new UsuarioCreateDTO(
                "Fe",
                "email-invalido",
                "123",
                "senha123",
                1L
        );

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Erro de validação")))
                .andExpect(jsonPath("$.errors", notNullValue()))
                .andExpect(jsonPath("$.errors.nome", is("O nome deve ter entre 3 e 150 caracteres")))
                .andExpect(jsonPath("$.errors.email", is("E-mail no formato inválido")))
                .andExpect(jsonPath("$.errors.cpf", is("O cpf deve ter 11 caracteres")));

        verify(usuarioService, never()).cadastrarUsuario(any(UsuarioCreateDTO.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void atualizar_DeveAtualizarUsuario_QuandoDadosValidos() throws Exception {
        // Arrange
        UsuarioResponseDTO usuarioAtualizadoDTO = new UsuarioResponseDTO(
                1L,
                usuarioUpdateDTO.nome(),
                usuarioUpdateDTO.email(),
                usuarioUpdateDTO.cpf(),
                usuarioUpdateDTO.status(),
                TipoPerfil.USUARIO
        );

        when(usuarioService.atualizarUsuario(eq(1L), any(UsuarioUpdateDTO.class)))
                .thenReturn(usuarioAtualizadoDTO);

        // Act & Assert
        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_usuario", is(1)))
                .andExpect(jsonPath("$.nome", is("João Silva Atualizado")))
                .andExpect(jsonPath("$.email", is("joao.atualizado@email.com")))
                .andExpect(jsonPath("$.cpf", is("98765432101")))
                .andExpect(jsonPath("$.status", is("ATIVO")));

        verify(usuarioService, times(1)).atualizarUsuario(eq(1L), any(UsuarioUpdateDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar usuário inexistente")
    void atualizar_DeveRetornar404_QuandoIdNaoExistir() throws Exception {
        // Arrange
        when(usuarioService.atualizarUsuario(eq(999L), any(UsuarioUpdateDTO.class)))
                .thenThrow(new EntityNotFoundException("Usuário não encontrado com ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioUpdateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Usuário não encontrado com ID: 999")));

        verify(usuarioService, times(1)).atualizarUsuario(eq(999L), any(UsuarioUpdateDTO.class));
    }

    @Test
    @DisplayName("Deve remover usuário com sucesso")
    void remover_DeveRemoverUsuario_QuandoIdExistir() throws Exception {
        // Arrange
        doNothing().when(usuarioService).deletarUsuario(1L);

        // Act & Assert
        mockMvc.perform(delete("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService, times(1)).deletarUsuario(1L);
    }

    @Test
    @DisplayName("Deve retornar 404 ao remover usuário inexistente")
    void remover_DeveRetornar404_QuandoIdNaoExistir() throws Exception {
        // Arrange
        doThrow(new EntityNotFoundException("Usuário não encontrado com ID: 999"))
                .when(usuarioService).deletarUsuario(999L);

        // Act & Assert
        mockMvc.perform(delete("/usuarios/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Usuário não encontrado com ID: 999")));

        verify(usuarioService, times(1)).deletarUsuario(999L);
    }

    @Test
    @DisplayName("Deve validar tamanho mínimo dos campos")
    void validarTamanhoMinimo_Campos() throws Exception {
        // Arrange
        usuarioCreateDTO = new UsuarioCreateDTO(
                "ab",                 // nome inválido (<3)
                "teste@email.com",   // email válido
                "12345678901",       // cpf válido
                "123",               // senha inválida (<6)
                1L                   // perfil válido
        );

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioCreateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors.nome", is("O nome deve ter entre 3 e 150 caracteres")))
                .andExpect(jsonPath("$.errors.senha", is("A senha deve ter entre 6 e 100 caracteres")));

        verify(usuarioService, never()).cadastrarUsuario(any(UsuarioCreateDTO.class));
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios no cadastro")
    void validarCamposObrigatorios_Cadastro() throws Exception {
        // Arrange
        UsuarioCreateDTO dtoVazio = new UsuarioCreateDTO(
                null,
                null,
                null,
                null,
                null
        );

        // Act & Assert
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoVazio)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Erro de validação")))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.errors.nome", is("Nome é obrigatório")))
                .andExpect(jsonPath("$.errors.email", is("Email é obrigatório")))
                .andExpect(jsonPath("$.errors.cpf", is("CPF é obrigatório")))
                .andExpect(jsonPath("$.errors.senha", is("A senha é obrigatória")))
                .andExpect(jsonPath("$.errors.perfilId", is("Perfil é obrigatório")));

        verify(usuarioService, never()).cadastrarUsuario(any(UsuarioCreateDTO.class));
    }
}