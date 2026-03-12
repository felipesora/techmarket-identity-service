package br.com.techmarket_identity_service.controller;

import br.com.techmarket_identity_service.dto.login.LoginRequestDTO;
import br.com.techmarket_identity_service.dto.login.LoginResponseDTO;
import br.com.techmarket_identity_service.model.Usuario;
import br.com.techmarket_identity_service.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AutenticacaoController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid LoginRequestDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = jwtService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(tokenJWT));
    }
}
