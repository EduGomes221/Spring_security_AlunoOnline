package br.com.alunoonline.api.dtos;

public record DadosPerfilUsuario(
        Long usuarioId,
        String login,
        AlunoVinculado aluno) {

    public record AlunoVinculado(Long id, String nomeCompleto, String email) {
    }
}
