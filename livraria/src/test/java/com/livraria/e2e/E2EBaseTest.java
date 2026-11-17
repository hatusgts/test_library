package com.livraria.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

@Tag("e2e")
abstract class E2EBaseTest {

    protected static final String SENHA = "123456";
    private static final String URL_BASE = System.getProperty("e2e.url", "http://localhost:4200");

    protected WebDriver driver;
    protected WebDriverWait espera;

    @BeforeEach
    void abrirNavegador() {
        ChromeOptions opcoes = new ChromeOptions();
        opcoes.addArguments("--window-size=1366,900", "--no-sandbox", "--remote-allow-origins=*");
        if (!"false".equals(System.getProperty("e2e.headless"))) {
            opcoes.addArguments("--headless=new");
        }
        driver = new ChromeDriver(opcoes);
        espera = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected String sufixo() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    protected void abrir(String caminho) {
        driver.get(URL_BASE + caminho);
    }

    protected WebElement campo(String id) {
        return espera.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    }

    protected void preencher(String id, String valor) {
        campo(id).sendKeys(Keys.chord(Keys.CONTROL, "a"), valor);
    }

    protected void clicar(String id) {
        espera.until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
    }

    protected By linha(String texto) {
        return By.xpath("//table[@id='tabela']//tr[td[normalize-space()='" + texto + "']]");
    }

    protected void aguardarLinha(String texto) {
        espera.until(ExpectedConditions.visibilityOfElementLocated(linha(texto)));
    }

    protected void aguardarSemLinha(String texto) {
        espera.until(ExpectedConditions.invisibilityOfElementLocated(linha(texto)));
    }

    protected void clicarNaLinha(String texto, String classeBotao) {
        espera.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//table[@id='tabela']//tr[td[normalize-space()='" + texto + "']]//button[contains(@class,'"
                        + classeBotao + "')]"))).click();
    }

    protected void cadastrarUsuarioPelaTela(String nome, String email) {
        abrir("/cadastro");
        preencher("campo-nome", nome);
        preencher("campo-email", email);
        preencher("campo-senha", SENHA);
        clicar("btn-cadastrar");
        espera.until(ExpectedConditions.urlContains("/login"));
    }

    protected void fazerLogin(String email, String senha) {
        preencher("campo-email", email);
        preencher("campo-senha", senha);
        clicar("btn-entrar");
    }

    protected String novoUsuarioLogado(String nome) {
        String email = "e2e-" + sufixo() + "@livraria.com";
        cadastrarUsuarioPelaTela(nome, email);
        fazerLogin(email, SENHA);
        espera.until(ExpectedConditions.urlContains("/livros"));
        return email;
    }
}
