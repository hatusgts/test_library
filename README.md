# Livraria

Sistema web de vendas de livros, desenvolvido para a avaliação de Testes de Software (FATESG, 2026/02, Prof. Reinaldo Jr).

## Integrantes

- Hyan
- Hatus
- Gilberto

## Proposta

Sistema para uma livraria: o funcionário cria uma conta, entra no sistema e mantém o catálogo de livros e a base de clientes. Há três CRUDs completos:

- Livro: título, autor, preço, categoria, estoque
- Cliente: nome, e-mail, CPF, telefone, cidade
- Usuário: nome, e-mail, senha (com tela de login)

Todas as rotas, exceto login e cadastro de usuário, exigem autenticação. A senha é gravada com BCrypt.

## Tecnologias

- Back-end: Java 21, Spring Boot 4 (Web, Data JPA, Security, Validation)
- Banco de dados: PostgreSQL
- Front-end: Angular 22
- Testes: JUnit 5 e Mockito, REST-assured, Selenium

## Estrutura

- livraria/: API Spring Boot e todos os testes
- frontend/: aplicação Angular

## Como executar

Pré-requisitos: JDK 21, Node.js 22.22 ou superior, PostgreSQL e Google Chrome (só para o Selenium).

1. Crie o banco:

```sql
CREATE USER livraria WITH PASSWORD 'livraria123';
CREATE DATABASE livraria OWNER livraria;
CREATE DATABASE livraria_test OWNER livraria;
```

A conexão padrão é localhost:5432 e pode ser alterada com as variáveis DB_URL, DB_USER e DB_PASSWORD.

2. Suba a API em http://localhost:8080:

```bash
cd livraria
./mvnw spring-boot:run
```

3. Suba o front-end em http://localhost:4200:

```bash
cd frontend
npm install
npm start
```

Na tela de login, use "Cadastre-se" para criar o primeiro usuário.

## Testes

Unitários (12) e de API (7), que só precisam do banco livraria_test:

```bash
cd livraria
./mvnw test
```

E2E com Selenium (7), que precisam da API e do front-end rodando:

```bash
cd livraria
./mvnw test -Pe2e
./mvnw test -Pe2e -De2e.headless=false   # com o navegador visível
```

Os testes ficam em livraria/src/test/java/com/livraria/, nas pastas service (unitários), api (REST-assured) e e2e (Selenium).
