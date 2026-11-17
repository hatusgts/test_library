package com.livraria.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;

class LivroApiTest extends ApiBaseTest {

    private String email;

    @BeforeEach
    void cadastrar() {
        email = cadastrarUsuario();
    }

    private Map<String, Object> livro(String titulo) {
        return Map.of("titulo", titulo, "autor", "Machado de Assis", "preco", 29.9,
                "categoria", "Romance", "estoque", 5);
    }

    @Test
    void deveExecutarCrudCompletoDeLivro() {
        int id = autenticado(email).body(livro("Dom Casmurro"))
                .post("/api/livros")
                .then().statusCode(201)
                .body("titulo", equalTo("Dom Casmurro"))
                .extract().path("id");

        autenticado(email).get("/api/livros/" + id)
                .then().statusCode(200).body("autor", equalTo("Machado de Assis"));

        autenticado(email).body(livro("Memórias Póstumas"))
                .put("/api/livros/" + id)
                .then().statusCode(200).body("titulo", equalTo("Memórias Póstumas"));

        autenticado(email).get("/api/livros")
                .then().statusCode(200).body("id", hasItem(id));

        autenticado(email).delete("/api/livros/" + id).then().statusCode(204);

        autenticado(email).get("/api/livros/" + id).then().statusCode(404);
    }

    @Test
    void deveRejeitarLivroInvalido() {
        autenticado(email)
                .body(Map.of("titulo", "", "autor", "X", "preco", -1, "categoria", "Y", "estoque", 1))
                .post("/api/livros")
                .then().statusCode(400)
                .body("erros.titulo", notNullValue())
                .body("erros.preco", notNullValue());
    }
}
