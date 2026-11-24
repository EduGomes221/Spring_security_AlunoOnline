package br.com.alunoonline.api.service;

import br.com.alunoonline.api.model.Aluno;
import br.com.alunoonline.api.model.Usuario;
import br.com.alunoonline.api.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilUsuarioServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private PerfilUsuarioService perfilUsuarioService;

    @Test
    void returnsOnlyBasicFieldsForAuthenticatedUsersProfile() {
        Usuario usuario = new Usuario(3L, "carla", "senha-hash");
        Aluno aluno = new Aluno();
        aluno.setId(9L);
        aluno.setNomeCompleto("Carla Silva");
        aluno.setEmail("carla@example.test");
        aluno.setCpf("12345678900");
        when(alunoRepository.findByUsuarioId(3L)).thenReturn(Optional.of(aluno));

        var perfil = perfilUsuarioService.obterPerfil(usuario);

        assertEquals(3L, perfil.usuarioId());
        assertEquals("carla", perfil.login());
        assertEquals(9L, perfil.aluno().id());
        assertEquals("Carla Silva", perfil.aluno().nomeCompleto());
        assertEquals("carla@example.test", perfil.aluno().email());
    }

    @Test
    void returnsNullStudentWhenAccountHasNoLinkedStudent() {
        Usuario usuario = new Usuario(3L, "carla", "senha-hash");
        when(alunoRepository.findByUsuarioId(3L)).thenReturn(Optional.empty());

        var perfil = perfilUsuarioService.obterPerfil(usuario);

        assertNull(perfil.aluno());
    }
}
