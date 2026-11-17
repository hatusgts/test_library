package com.livraria.service;

import com.livraria.exception.RecursoNaoEncontradoException;
import com.livraria.model.Livro;
import com.livraria.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository repository;

    public List<Livro> listar() {
        return repository.findAll();
    }

    public Livro buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado"));
    }

    public Livro criar(Livro livro) {
        livro.setId(null);
        return repository.save(livro);
    }

    public Livro atualizar(Long id, Livro dados) {
        buscar(id);
        dados.setId(id);
        return repository.save(dados);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }
}
