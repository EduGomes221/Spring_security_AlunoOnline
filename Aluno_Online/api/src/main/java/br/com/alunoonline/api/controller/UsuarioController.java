package br.com.alunoonline.api.controller;

import br.com.alunoonline.api.dtos.DadosCadastroUsuario;
import br.com.alunoonline.api.dtos.DadosDetalhamentoUsuario;
import br.com.alunoonline.api.dtos.DadosPerfilUsuario;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.repository.UsuarioRepository;
import br.com.alunoonline.api.service.PerfilUsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;
    private final PerfilUsuarioService perfilUsuarioService;

    @GetMapping("/me")
    @SecurityRequirement(name = "bearer-key")
    public DadosPerfilUsuario meuPerfil(@AuthenticationPrincipal Usuario usuario) {
        return perfilUsuarioService.obterPerfil(usuario);
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        // Verificar se o login já está cadastrado
        if (usuarioRepository.findByLogin(dados.login()) != null) {
            return ResponseEntity.badRequest().body("Login já cadastrado.");
        }

        // Criptografar a senha
        var senhaCriptografada = passwordEncoder.encode(dados.senha());

        // Criar o usuário com o login e senha criptografada
        var usuario = new Usuario(dados.login(), senhaCriptografada);

        // Salvar o usuário no banco de dados
        usuarioRepository.save(usuario);

        // Criar o URI para o novo usuário
        var uri = uriBuilder.path("/usuarios/{id}")
                .buildAndExpand(usuario.getId()).toUri();

        // Retornar resposta com status 201 e os dados do usuário criado
        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }
}
