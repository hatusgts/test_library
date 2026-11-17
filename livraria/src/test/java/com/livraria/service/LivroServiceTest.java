package com.livraria.service;

import com.livraria.exception.RecursoNaoEncontradoException;
import com.livraria.model.Livro;
import com.livraria.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository repository;

    @InjectMocks
    private LivroService service;

    private Livro livro() {
        return new Livro(7L, "Dom Casmurro", "Machado de Assis", new BigDecimal("29.90"), "Romance", 5);
    }

    @Test
    void deveLancarExcecaoQuandoLivroNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(99L));
    }

    @Test
    void deveIgnorarIdAoCriarLivro() {
        when(repository.save(any(Livro.class))).thenAnswer(i -> i.getArgument(0));

        Livro criado = service.criar(livro());

        assertNull(criado.getId());
    }

    @Test
    void deveManterIdDaUrlAoAtualizarLivro() {
        Livro dados = livro();
        dados.setId(null);
        dados.setPreco(new BigDecimal("39.90"));
        when(repository.findById(7L)).thenReturn(Optional.of(livro()));
        when(repository.save(any(Livro.class))).thenAnswer(i -> i.getArgument(0));

        Livro atualizado = service.atualizar(7L, dados);

        assertEquals(7L, atualizado.getId());
        assertEquals(new BigDecimal("39.90"), atualizado.getPreco());
    }

    @Test
    void naoDeveExcluirLivroInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.excluir(99L));
        verify(repository, never()).delete(any());
    }
}
