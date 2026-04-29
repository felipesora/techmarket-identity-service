package br.com.techmarket_identity_service.repository;

import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.model.enums.StatusUsuario;
import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByCpfAndPerfil(String cpf, TipoPerfil perfil);

    Page<Usuario> findByPerfil(TipoPerfil perfil, Pageable pageable);

    long countByStatusAndPerfil(StatusUsuario status, TipoPerfil perfil);
}
