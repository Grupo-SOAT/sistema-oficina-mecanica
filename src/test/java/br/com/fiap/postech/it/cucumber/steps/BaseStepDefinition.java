package br.com.fiap.postech.it.cucumber.steps;

import br.com.fiap.postech.it.cucumber.context.CucumberTestContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public abstract class BaseStepDefinition {
    protected CucumberTestContext context;

    public BaseStepDefinition() {
        this.context = CucumberTestContext.getInstance();
    }

    protected MockMvc mockMvc;

    // Helpers

    protected void performLoginAs(String role) {
        String username = role.toLowerCase();
        context.setCurrentUser(username);
        context.setCurrentRole(role.toLowerCase());
        context.setCurrentPassword(username);
        context.setCurrentApiKey(null);
        context.setAuthToken(null);
        context.setLastStatusCode(200);
        context.setLastResponseBody("{}");
    }

    protected MockHttpServletRequestBuilder withBearerAuth(MockHttpServletRequestBuilder request) {
        if (context.getAuthToken() != null && !context.getAuthToken().isBlank()) {
            request.header("Authorization", "Bearer " + context.getAuthToken());
        }
        return request;
    }

    protected MockHttpServletRequestBuilder withRoleAuth(MockHttpServletRequestBuilder request) {
        if (context.getCurrentRole() == null || context.getCurrentRole().isBlank()) {
            return request;
        }

        String username = context.getCurrentUser() != null ? context.getCurrentUser() : context.getCurrentRole();
        return request.with(SecurityMockMvcRequestPostProcessors.user(username).roles(toSpringRole(context.getCurrentRole())));
    }

    protected boolean hasRoleAuth() {
        return context.getCurrentRole() != null && !context.getCurrentRole().isBlank();
    }

    private String toSpringRole(String role) {
        return switch (role.toLowerCase()) {
            case "admin" -> "ADMIN";
            case "mecanico" -> "MECHANIC";
            case "atendente" -> "ATTENDANT";
            case "almoxarife" -> "STOREKEEPER";
            case "chatbot" -> "CHATBOT";
            default -> role.toUpperCase();
        };
    }

    protected void performPost(String url, String body, String token) {
        try {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
                    .with(SecurityMockMvcRequestPostProcessors.csrf());
            if (token != null) request.header("Authorization", "Bearer " + token);
            MvcResult result = mockMvc.perform(request).andReturn();
            context.setLastStatusCode(result.getResponse().getStatus());
            context.setLastResponseBody(result.getResponse().getContentAsString());
            context.setLastResponseContentType(result.getResponse().getContentType());
        } catch (Exception e) {
            context.setLastStatusCode(0);
            context.setLastResponseBody(e.getMessage());
            context.setLastResponseContentType(null);
        }
    }

    protected void performGet(String url, String token) {
        try {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);
            if (token != null) request.header("Authorization", "Bearer " + token);
            MvcResult result = mockMvc.perform(request).andReturn();
            context.setLastStatusCode(result.getResponse().getStatus());
            context.setLastResponseBody(result.getResponse().getContentAsString());
            context.setLastResponseContentType(result.getResponse().getContentType());
        } catch (Exception e) {
            context.setLastStatusCode(0);
            context.setLastResponseBody(e.getMessage());
            context.setLastResponseContentType(null);
        }
    }

    protected MockHttpServletRequestBuilder buildRequest(String method, String url) {
        return switch (method.toUpperCase()) {
            case "GET" -> MockMvcRequestBuilders.get(url);
            case "POST" -> MockMvcRequestBuilders.post(url);
            case "PATCH" -> MockMvcRequestBuilders.patch(url);
            case "PUT" -> MockMvcRequestBuilders.put(url);
            case "DELETE" -> MockMvcRequestBuilders.delete(url);
            default -> throw new IllegalArgumentException("Método HTTP não suportado: " + method);
        };
    }

    protected String extractJsonField(JsonNode body, String field) {
        if (body == null || body.isNull()) {
            return null;
        }

        JsonNode valueNode = body.path(field);
        if (valueNode.isMissingNode() || valueNode.isNull()) {
            return null;
        }

        return valueNode.isValueNode() ? valueNode.asText() : valueNode.toString();
    }

    protected void executeWithOptionalAuth(String method, String url, String body) {
        try {
            MockHttpServletRequestBuilder request = withRoleAuth(buildRequest(method, url)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            if (body != null) {
                request.content(body);
            }
            if (hasRoleAuth() && !"GET".equalsIgnoreCase(method)) {
                request.with(SecurityMockMvcRequestPostProcessors.csrf());
            }

            MvcResult result = mockMvc.perform(request).andReturn();
            context.setLastStatusCode(result.getResponse().getStatus());
            context.setLastResponseBody(result.getResponse().getContentAsString());
            context.setLastResponseContentType(result.getResponse().getContentType());
        } catch (Exception e) {
            context.setLastStatusCode(0);
            context.setLastResponseBody(e.getMessage());
            context.setLastResponseContentType(null);
        }
    }

    protected static <T> Iterable<T> iterable(java.util.Iterator<T> iterator) {
        return () -> iterator;
    }
}
