package br.com.techmarket_identity_service.repository;

import br.com.techmarket_identity_service.model.Perfil;
import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByTipoPerfil(TipoPerfil tipoPerfil);
}
