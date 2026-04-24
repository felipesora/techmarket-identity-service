package br.com.techmarket_identity_service.config;

import br.com.techmarket_identity_service.dto.usuario.UsuarioCreateDTO;
import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import br.com.techmarket_identity_service.repository.UsuarioRepository;
import br.com.techmarket_identity_service.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public DataSeeder(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsuarios();
    }

    private void seedUsuarios() {
        cadastrarUsuarioSeNaoExistir("Usuário Admin", "admin@techmarket.com", "55871982019", "admin123", TipoPerfil.ADMINISTRADOR);
    }

    private void cadastrarUsuarioSeNaoExistir(String nome, String email, String cpf, String senha, TipoPerfil perfil) {
        boolean existe = usuarioRepository.existsByEmail(email);

        if (!existe) {
            UsuarioCreateDTO dto = new UsuarioCreateDTO(nome, email, cpf, senha, perfil);
            usuarioService.cadastrarUsuario(dto);
            System.out.println("Usuario cadastrado: " + email);
        } else {
            System.out.println("Usuario já cadastrado: " + email);
        }
    }
}
