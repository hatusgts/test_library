package com.livraria.e2e;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LivroE2ETest extends E2EBaseTest {

    @Test
    void deveCriarEditarEExcluirLivroPelaTela() {
        novoUsuarioLogado("Livreiro");
        String titulo = "Livro " + sufixo();
        String tituloEditado = titulo + " (2a ed.)";

        preencher("campo-titulo", titulo);
        preencher("campo-autor", "Machado de Assis");
        preencher("campo-preco", "29.9");
        preencher("campo-categoria", "Romance");
        preencher("campo-estoque", "5");
        clicar("btn-salvar");
        aguardarLinha(titulo);

        clicarNaLinha(titulo, "editar");
        preencher("campo-titulo", tituloEditado);
        preencher("campo-estoque", "12");
        clicar("btn-salvar");
        aguardarLinha(tituloEditado);
        assertTrue(driver.findElement(linha(tituloEditado)).getText().contains("12"));

        clicarNaLinha(tituloEditado, "excluir");
        aguardarSemLinha(tituloEditado);
    }

    @Test
    void deveBloquearCadastroDeLivroComCamposVazios() {
        novoUsuarioLogado("Livreiro");

        preencher("campo-titulo", "Somente titulo");

        assertTrue(!campo("btn-salvar").isEnabled());
    }
}
