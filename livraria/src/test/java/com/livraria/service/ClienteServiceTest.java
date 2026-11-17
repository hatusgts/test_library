package com.livraria.service;

import com.livraria.exception.RecursoNaoEncontradoException;
import com.livraria.model.Cliente;
import com.livraria.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private Cliente cliente() {
        return new Cliente(3L, "Maria", "maria@email.com", "12345678900", "62999999999", "Goiânia");
    }

    @Test
    void deveListarClientes() {
        when(repository.findAll()).thenReturn(List.of(cliente()));

        assertEquals(1, service.listar().size());
    }

    @Test
    void deveBuscarClientePorId() {
        when(repository.findById(3L)).thenReturn(Optional.of(cliente()));

        assertEquals("Maria", service.buscar(3L).getNome());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscar(99L));
    }

    @Test
    void deveExcluirClienteExistente() {
        Cliente existente = cliente();
        when(repository.findById(3L)).thenReturn(Optional.of(existente));

        service.excluir(3L);

        verify(repository).delete(existente);
    }
}
