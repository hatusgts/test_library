package com.livraria.e2e;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClienteE2ETest extends E2EBaseTest {

    @Test
    void deveCriarEditarEExcluirClientePelaTela() {
        novoUsuarioLogado("Atendente");
        espera.until(ExpectedConditions.elementToBeClickable(By.linkText("Clientes"))).click();
        espera.until(ExpectedConditions.urlContains("/clientes"));

        String sufixo = sufixo();
        String nome = "Cliente " + sufixo;
        preencher("campo-nome", nome);
        preencher("campo-email", "cliente-" + sufixo + "@email.com");
        preencher("campo-cpf", sufixo);
        preencher("campo-telefone", "62999999999");
        preencher("campo-cidade", "Goiânia");
        clicar("btn-salvar");
        aguardarLinha(nome);

        clicarNaLinha(nome, "editar");
        preencher("campo-cidade", "Anápolis");
        clicar("btn-salvar");
        espera.until(ExpectedConditions.textToBePresentInElementLocated(linha(nome), "Anápolis"));

        clicarNaLinha(nome, "excluir");
        aguardarSemLinha(nome);
    }

    @Test
    void deveExibirErroAoCadastrarClienteComEmailDuplicado() {
        novoUsuarioLogado("Atendente");
        espera.until(ExpectedConditions.elementToBeClickable(By.linkText("Clientes"))).click();
        espera.until(ExpectedConditions.urlContains("/clientes"));

        String sufixo = sufixo();
        String email = "dup-" + sufixo + "@email.com";
        cadastrar("Primeiro " + sufixo, email, "A" + sufixo);
        aguardarLinha("Primeiro " + sufixo);

        cadastrar("Segundo " + sufixo, email, "B" + sufixo);

        assertEquals("Registro duplicado ou inválido", campo("mensagem-erro").getText());

        clicarNaLinha("Primeiro " + sufixo, "excluir");
        aguardarSemLinha("Primeiro " + sufixo);
    }

    private void cadastrar(String nome, String email, String cpf) {
        preencher("campo-nome", nome);
        preencher("campo-email", email);
        preencher("campo-cpf", cpf);
        preencher("campo-telefone", "62999999999");
        preencher("campo-cidade", "Goiânia");
        clicar("btn-salvar");
    }
}
