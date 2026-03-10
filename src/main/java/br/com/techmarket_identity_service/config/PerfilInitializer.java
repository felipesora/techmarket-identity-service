package br.com.techmarket_identity_service.config;

import br.com.techmarket_identity_service.model.Perfil;
import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import br.com.techmarket_identity_service.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PerfilInitializer implements CommandLineRunner {

    private final PerfilRepository perfilRepository;

    @Override
    public void run(String... args) {

        if (perfilRepository.findByTipoPerfil(TipoPerfil.ADMINISTRADOR).isEmpty()) {
            Perfil admin = new Perfil();
            admin.setTipoPerfil(TipoPerfil.ADMINISTRADOR);
            perfilRepository.save(admin);
        }

        if (perfilRepository.findByTipoPerfil(TipoPerfil.USUARIO).isEmpty()) {
            Perfil usuario = new Perfil();
            usuario.setTipoPerfil(TipoPerfil.USUARIO);
            perfilRepository.save(usuario);
        }
    }
}
