package com.livraria.service;

import com.livraria.exception.RecursoNaoEncontradoException;
import com.livraria.exception.RegraNegocioException;
import com.livraria.model.Usuario;
import com.livraria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    public Usuario criar(Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado");
        }
        usuario.setId(null);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario atual = buscar(id);
        repository.findByEmail(dados.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new RegraNegocioException("E-mail já cadastrado");
                });
        atual.setNome(dados.getNome());
        atual.setEmail(dados.getEmail());
        atual.setSenha(encoder.encode(dados.getSenha()));
        return repository.save(atual);
    }

    public void excluir(Long id) {
        repository.delete(buscar(id));
    }

    public Usuario autenticar(String email, String senha) {
        return repository.findByEmail(email)
                .filter(u -> encoder.matches(senha, u.getSenha()))
                .orElseThrow(() -> new RegraNegocioException("Credenciais inválidas"));
    }
}
