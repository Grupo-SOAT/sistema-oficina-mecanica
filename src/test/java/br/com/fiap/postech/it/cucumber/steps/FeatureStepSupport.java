package br.com.fiap.postech.it.cucumber.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class FeatureStepSupport extends BaseStepDefinition {
    @Autowired
    protected WebApplicationContext webContext;

    @Autowired
    protected DataSource dataSource;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected void initializeFeature() {
        context.reset();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();
    }

    protected void initializeFeature(String... domains) {
        context.reset();
        if (domains != null) {
            for (String domain : domains) {
                runDomainSeed(domain);
            }
        }
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();
    }

    protected void runDomainSeed(String domain) {
        if (domain == null || domain.isBlank()) {
            return;
        }
        String seedPath = "db/" + domain + "-seed.sql";
        ClassPathResource seedResource = new ClassPathResource(seedPath);
        if (!seedResource.exists()) {
            return;
        }

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(seedResource);
        populator.execute(dataSource);
    }

    protected Map<String, String> paginationParams() {
        Map<String, String> params = new LinkedHashMap<>();
        if (context.getPageSize() != null) {
            params.put("pageSize", String.valueOf(context.getPageSize()));
        }
        if (context.getCursor() != null) {
            params.put("cursor", context.getCursor());
        }
        if (context.getFilterName() != null && context.getFilterValue() != null) {
            params.put(context.getFilterName(), context.getFilterValue());
        }
        return params;
    }

    protected void executeRequest(String method, String url, String body, Map<String, String> params) {
        try {
            MockHttpServletRequestBuilder request = buildRequest(method, url)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);

            if (body != null) {
                request.content(body);
            }
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    request.param(entry.getKey(), entry.getValue());
                }
            }

            request = withRoleAuth(request);
            if (hasRoleAuth() && !"GET".equalsIgnoreCase(method)) {
                request.with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf());
            }

            MvcResult result = mockMvc.perform(request).andReturn();
            context.setLastStatusCode(result.getResponse().getStatus());
            context.setLastResponseBody(result.getResponse().getContentAsString());
        } catch (Exception e) {
            context.setLastStatusCode(0);
            context.setLastResponseBody(e.getMessage());
        }
    }

    protected String dataTableToJson(DataTable table) throws Exception {
        Map<String, String> row = table.asMaps(String.class, String.class).get(0);
        Map<String, Object> normalized = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : row.entrySet()) {
            String value = entry.getValue();
            if (value == null || value.isBlank()) {
                normalized.put(entry.getKey(), null);
                continue;
            }
            if (value.matches("^-?\\d+$")) {
                normalized.put(entry.getKey(), Long.parseLong(value));
            } else if (value.matches("^-?\\d+\\.\\d+$")) {
                normalized.put(entry.getKey(), Double.parseDouble(value));
            } else {
                normalized.put(entry.getKey(), value);
            }
        }

        return objectMapper.writeValueAsString(normalized);
    }

    protected void setJsonBody(DataTable table) throws Exception {
        context.setRequestBody(dataTableToJson(table));
    }

    protected void assertDataArray() throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");
    }

    protected void assertMaxDataSize(int maxSize, String resourceName) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");
        assertTrue(root.get("data").size() <= maxSize,
                "Quantidade de " + resourceName + " acima do esperado: " + root.get("data").size());
    }

    protected void assertHasItemWithField(String field, String expectedValue, String resourceName) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");

        boolean found = false;
        for (JsonNode item : root.get("data")) {
            if (item.has(field) && expectedValue.equals(item.get(field).asText())) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Nenhum " + resourceName + " com " + field + " encontrado: " + expectedValue);
    }

    protected void assertFirstItemIdGreaterThan(long minId, String resourceName) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");
        assertFalse(root.get("data").isEmpty(), "Lista de " + resourceName + " vazia");
        assertTrue(root.get("data").get(0).has("id"), "Primeiro item sem id");
        assertTrue(root.get("data").get(0).get("id").asLong() > minId,
                "Id do primeiro item nao respeita cursor");
    }

    protected void assertResponseMatchesRequest() throws Exception {
        JsonNode request = objectMapper.readTree(context.getRequestBody());
        JsonNode response = context.getLastResponseBody();

        for (String fieldName : iterable(request.fieldNames())) {
            JsonNode expectedValue = request.get(fieldName);
            assertTrue(response.has(fieldName), "Campo ausente na resposta: " + fieldName);
            assertEquals(expectedValue.asText(), response.get(fieldName).asText(),
                    "Valor divergente para campo: " + fieldName);
        }
    }
}