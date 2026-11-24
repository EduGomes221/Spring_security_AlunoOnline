package br.com.alunoonline.api.service;

import br.com.alunoonline.api.dtos.DadosPerfilUsuario;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilUsuarioService {

    private final AlunoRepository alunoRepository;

    public DadosPerfilUsuario obterPerfil(Usuario usuario) {
        var alunoVinculado = alunoRepository.findByUsuarioId(usuario.getId())
                .map(aluno -> new DadosPerfilUsuario.AlunoVinculado(
                        aluno.getId(), aluno.getNomeCompleto(), aluno.getEmail()))
                .orElse(null);

        return new DadosPerfilUsuario(usuario.getId(), usuario.getLogin(), alunoVinculado);
    }
}
