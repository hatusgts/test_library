package com.livraria.service;

import com.livraria.exception.RegraNegocioException;
import com.livraria.model.Usuario;
import com.livraria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(repository, encoder);
    }

    private Usuario usuario() {
        return new Usuario(null, "Ana", "ana@livraria.com", "123456");
    }

    @Test
    void deveCriptografarSenhaAoCriarUsuario() {
        when(repository.existsByEmail("ana@livraria.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario criado = service.criar(usuario());

        assertNotEquals("123456", criado.getSenha());
        assertTrue(encoder.matches("123456", criado.getSenha()));
    }

    @Test
    void naoDeveCriarUsuarioComEmailDuplicado() {
        when(repository.existsByEmail("ana@livraria.com")).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> service.criar(usuario()));
        verify(repository, never()).save(any());
    }

    @Test
    void deveAutenticarComCredenciaisValidas() {
        Usuario salvo = usuario();
        salvo.setSenha(encoder.encode("123456"));
        when(repository.findByEmail("ana@livraria.com")).thenReturn(Optional.of(salvo));

        Usuario autenticado = service.autenticar("ana@livraria.com", "123456");

        assertEquals("Ana", autenticado.getNome());
    }

    @Test
    void naoDeveAutenticarComSenhaIncorreta() {
        Usuario salvo = usuario();
        salvo.setSenha(encoder.encode("123456"));
        when(repository.findByEmail("ana@livraria.com")).thenReturn(Optional.of(salvo));

        assertThrows(RegraNegocioException.class, () -> service.autenticar("ana@livraria.com", "errada"));
    }
}
