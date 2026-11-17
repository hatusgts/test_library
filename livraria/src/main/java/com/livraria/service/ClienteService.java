package com.livraria.service;

import com.livraria.exception.RecursoNaoEncontradoException;
import com.livraria.model.Cliente;
import com.livraria.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public List<Cliente> listar() {
        return repository.findAll();
    }

    public Cliente buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    public Cliente criar(Cliente cliente) {
        cliente.setId(null);
        return repository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dados) {
        buscar(id);
        dados.setId(id);
        return repository.save(dados);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }
}
