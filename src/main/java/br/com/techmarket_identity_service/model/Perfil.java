package br.com.techmarket_identity_service.model;

import br.com.techmarket_identity_service.model.enums.TipoPerfil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TM_PERFIL")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tm_perfil_seq")
    @SequenceGenerator(name = "tm_perfil_seq", sequenceName = "tm_perfil_seq", allocationSize = 1)
    @Column(name = "id_perfil")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo_perfil")
    private TipoPerfil tipoPerfil;

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios;
}
