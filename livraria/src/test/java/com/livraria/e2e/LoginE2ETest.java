package com.livraria.e2e;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginE2ETest extends E2EBaseTest {

    @Test
    void deveCadastrarUsuarioFazerLoginEDeslogar() {
        novoUsuarioLogado("Leitor E2E");

        assertEquals("Leitor E2E", campo("usuario-logado").getText());

        clicar("btn-sair");
        espera.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.findElements(By.id("usuario-logado")).isEmpty());
    }

    @Test
    void deveExibirErroComSenhaIncorreta() {
        String email = "e2e-" + sufixo() + "@livraria.com";
        cadastrarUsuarioPelaTela("Leitor", email);

        fazerLogin(email, "senhaerrada");

        assertEquals("Credenciais inválidas", campo("mensagem-erro").getText());
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void deveRedirecionarParaLoginAoAcessarRotaProtegidaSemSessao() {
        abrir("/livros");

        espera.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"));
    }
}
