package com.livraria.controller;

import com.livraria.model.Usuario;
import com.livraria.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    public record LoginRequest(@NotBlank String email, @NotBlank String senha) {
    }

    private final UsuarioService service;

    @PostMapping("/login")
    public Usuario login(@Valid @RequestBody LoginRequest request) {
        return service.autenticar(request.email(), request.senha());
    }
}
