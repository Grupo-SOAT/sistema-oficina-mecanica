package br.com.fiap.postech.it.cucumber.steps;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class AuthenticationStepDefinitions extends BaseStepDefinition {
    @Autowired
    private WebApplicationContext webContext;

    @Before
    public void initialize() {
        context.reset();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();
    }

    // Autenticação com usuário e senha

    @Dado("que eu esteja deslogado")
    public void userIsLoggedOut() {
        context.setAuthToken(null);
        context.setCurrentUser(null);
        context.setCurrentRole(null);
    }

    @Dado("que eu esteja devidamente logado")
    public void userIsLoggedIn() {
        performLoginAs("admin");
    }

    @Quando("informo o usuário {string} e a senha {string}")
    public void submitCredentials(String username, String password) {
        context.setCurrentUser(username);
        context.setCurrentPassword(password);
        performPost("/auth/login",
                String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password),
                null);
    }

    @Então("devo receber uma resposta com status {string}")
    public void verifyStatusCode(String expectedStatus) {
        assertEquals(Integer.parseInt(expectedStatus), context.getLastStatusCode(),
                "Status inesperado. Body: " + context.getLastResponseBodyAsString());
    }

    @E("com um token de sessão válido")
    public void verifyValidSessionToken() {
        assertTrue(context.getLastStatusCode() < 300,
                "Status não é 2xx: " + context.getLastStatusCode());
        String token = extractJsonField(context.getLastResponseBody(), "token");
        assertNotNull(token, "Token ausente. Body: " + context.getLastResponseBodyAsString());
        assertFalse(token.isEmpty(), "Token vazio");
        context.setAuthToken(token);
    }

    @E("devo ser capaz de obter meus dados de usuário com {string} igual a {string}")
    public void verifyUserRoleField(String field, String expectedValue) {
        performGet("/auth/me", context.getAuthToken());
        assertEquals(200, context.getLastStatusCode(),
            "GET /auth/me falhou. Body: " + context.getLastResponseBodyAsString());
        String actual = extractJsonField(context.getLastResponseBody(), field);
        assertNotNull(actual, "Campo '" + field + "' ausente. Body: " + context.getLastResponseBodyAsString());
        assertEquals(expectedValue.toUpperCase(), actual.toUpperCase(),
                "Valor inesperado para '" + field + "'");
    }

    // Credenciais inválidas

    @Então("devo receber um erro com status {string}")
    public void verifyErrorStatus(String expectedStatus) {
        assertEquals(Integer.parseInt(expectedStatus), context.getLastStatusCode(),
                "Status de erro inesperado. Body: " + context.getLastResponseBodyAsString());
    }

    @E("com a mensagem de erro igual a {string}")
    public void verifyErrorMessage(String expectedMessage) {
        String reason = extractJsonField(context.getLastResponseBody(), "reason");
        assertEquals(expectedMessage, reason,
            "Mensagem de erro inesperada. Body: " + context.getLastResponseBodyAsString());
    }

    // Autenticação via API Key (chatbot)

    @Quando("informo a chave de API {string}")
    public void submitApiKey(String apiKey) {
        context.setCurrentApiKey(apiKey);
        try {
            MvcResult result = mockMvc.perform(
                    MockMvcRequestBuilders.post("/auth/chatbot")
                            .header("X-API-Key", apiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
            ).andReturn();
            context.setLastStatusCode(result.getResponse().getStatus());
            context.setLastResponseBody(result.getResponse().getContentAsString());
            context.setCurrentUser("chatbot");
            context.setCurrentRole("chatbot");
        } catch (Exception e) {
            context.setLastStatusCode(0);
            context.setLastResponseBody(e.getMessage());
        }
    }

    @E("devo ser capaz de obter o tempo de validade da minha chave de API")
    public void verifyApiKeyExpiration() {
        assertTrue(context.getLastStatusCode() < 300,
                "Status não é 2xx: " + context.getLastStatusCode());
        String expiresAt = extractJsonField(context.getLastResponseBody(), "expiresAt");
        assertNotNull(expiresAt, "expiresAt ausente. Body: " + context.getLastResponseBodyAsString());
        assertTrue(OffsetDateTime.parse(expiresAt).isAfter(OffsetDateTime.now()),
                "API Key já expirada: " + expiresAt);
    }

    // Autorização por role

    @Dado("que eu esteja logado como {string}")
    public void userIsLoggedInAs(String role) {
        performLoginAs(role);
    }

    @Dado("que eu esteja autenticado como {string}")
    public void userIsAuthenticatedAs(String role) {
        if ("chatbot".equals(role)) {
            submitApiKey("chatbot-123");
            context.setAuthToken(extractJsonField(context.getLastResponseBody(), "token"));
        } else {
            performLoginAs(role);
        }
    }

    @Quando("acesso o endpoint {string} {string}")
    public void accessEndpoint(String method, String endpoint) {
        String url = endpoint.replace(":serviceId", "1").replace(":id", "1");
        try {
            MockHttpServletRequestBuilder request = withRoleAuth(
                    buildRequest(method, url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"));
            if (hasRoleAuth() && !"GET".equalsIgnoreCase(method)) {
                request.with(SecurityMockMvcRequestPostProcessors.csrf());
            }
            MvcResult result = mockMvc.perform(request).andReturn();
            context.setLastStatusCode(result.getResponse().getStatus());
            context.setLastResponseBody(result.getResponse().getContentAsString());
        } catch (Exception e) {
            context.setLastStatusCode(0);
            context.setLastResponseBody(e.getMessage());
        }
    }

    @Então("devo receber uma resposta com status diferente de {string}")
    public void verifyStatusNotEqual(String forbiddenStatus) {
        assertNotEquals(Integer.parseInt(forbiddenStatus), context.getLastStatusCode(),
                "Endpoint não deveria retornar " + forbiddenStatus
                        + ". Body: " + context.getLastResponseBodyAsString());
    }
}
