package com.livraria.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AuthApiTest extends ApiBaseTest {

    @Test
    void deveCadastrarUsuarioEFazerLogin() {
        String email = cadastrarUsuario();

        given().contentType(ContentType.JSON)
                .body(Map.of("email", email, "senha", SENHA))
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("email", equalTo(email))
                .body("senha", nullValue());
    }

    @Test
    void deveRecusarLoginComSenhaIncorreta() {
        String email = cadastrarUsuario();

        given().contentType(ContentType.JSON)
                .body(Map.of("email", email, "senha", "errada"))
                .post("/api/auth/login")
                .then()
                .statusCode(409)
                .body("erro", equalTo("Credenciais inválidas"));
    }

    @Test
    void deveBloquearRotaProtegidaSemCredenciais() {
        given().get("/api/livros").then().statusCode(401);
    }

    @Test
    void deveRejeitarCadastroDeUsuarioComEmailDuplicado() {
        String email = cadastrarUsuario();

        given().contentType(ContentType.JSON)
                .body(Map.of("nome", "Outro", "email", email, "senha", SENHA))
                .post("/api/usuarios")
                .then().statusCode(409);
    }
}
