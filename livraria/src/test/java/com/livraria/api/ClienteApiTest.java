package com.livraria.api;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

class ClienteApiTest extends ApiBaseTest {

    @Test
    void deveExecutarCrudCompletoDeCliente() {
        String email = cadastrarUsuario();
        String sufixo = UUID.randomUUID().toString();
        Map<String, Object> cliente = Map.of("nome", "Maria", "email", "maria-" + sufixo + "@email.com",
                "cpf", sufixo, "telefone", "62999999999", "cidade", "Goiânia");

        int id = autenticado(email).body(cliente).post("/api/clientes")
                .then().statusCode(201).extract().path("id");

        autenticado(email).get("/api/clientes/" + id)
                .then().statusCode(200).body("cidade", equalTo("Goiânia"));

        autenticado(email).body(Map.of("nome", "Maria Silva", "email", "maria-" + sufixo + "@email.com",
                        "cpf", sufixo, "telefone", "62999999999", "cidade", "Anápolis"))
                .put("/api/clientes/" + id)
                .then().statusCode(200).body("cidade", equalTo("Anápolis"));

        autenticado(email).delete("/api/clientes/" + id).then().statusCode(204);
        autenticado(email).get("/api/clientes/" + id).then().statusCode(404);
    }
}
