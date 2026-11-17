package com.livraria.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class ApiBaseTest {

    protected static final String SENHA = "123456";

    @LocalServerPort
    private int port;

    @BeforeEach
    void configurar() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    protected String novoEmail() {
        return "user-" + UUID.randomUUID() + "@livraria.com";
    }

    protected String cadastrarUsuario() {
        String email = novoEmail();
        given().contentType(ContentType.JSON)
                .body(Map.of("nome", "Teste", "email", email, "senha", SENHA))
                .post("/api/usuarios")
                .then().statusCode(201);
        return email;
    }

    protected RequestSpecification autenticado(String email) {
        return given().auth().preemptive().basic(email, SENHA).contentType(ContentType.JSON);
    }
}
