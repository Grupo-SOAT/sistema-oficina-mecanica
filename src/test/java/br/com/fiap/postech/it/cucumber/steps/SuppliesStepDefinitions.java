package br.com.fiap.postech.it.cucumber.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class SuppliesStepDefinitions extends BaseStepDefinition {

    @Autowired
    private WebApplicationContext webContext;

    @Autowired
    private DataSource dataSource;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Integer pageSize;
    private String cursor;
    private String sku;
    private Long supplyId;
    private String requestBody;

    @Before("@supplies")
    public void initialize() {
        context.reset();
        resetSuppliesData();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();

        pageSize = null;
        cursor = null;
        sku = null;
        supplyId = null;
        requestBody = null;
    }

    private void resetSuppliesData() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("db/supplies-reset.sql"));
        populator.execute(dataSource);
    }

    @Dado("que o tamanho da pagina seja {int}")
    public void setPageSize(Integer value) {
        pageSize = value;
    }

    @Dado("que o cursor seja {string}")
    public void setCursor(String value) {
        cursor = value;
    }

    @Dado("que o filtro sku seja {string}")
    public void setSku(String value) {
        sku = value;
    }

    @Dado("que o id do insumo seja {long}")
    public void setSupplyId(Long id) {
        supplyId = id;
    }

    @Dado("que o id do insumo seja o ultimo criado")
    public void setSupplyIdAsLastCreated() throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("id"), "Resposta anterior nao contem id para reutilizar");
        supplyId = root.get("id").asLong();
    }

    @Dado("que o corpo do novo insumo seja:")
    public void setCreateBody(DataTable table) throws Exception {
        requestBody = dataTableToJson(table);
    }

    @Dado("que o corpo de atualizacao do insumo seja:")
    public void setUpdateBody(DataTable table) throws Exception {
        requestBody = dataTableToJson(table);
    }

    private String dataTableToJson(DataTable table) throws Exception {
        Map<String, String> row = table.asMaps(String.class, String.class).get(0);
        Map<String, Object> normalized = new HashMap<>();

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

    @Quando("eu listar os insumos")
    public void listSupplies() {
        try {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/supplies")
                    .accept(MediaType.APPLICATION_JSON);
            if (pageSize != null) {
                request.param("pageSize", String.valueOf(pageSize));
            }
            if (cursor != null) {
                request.param("cursor", cursor);
            }
            if (sku != null) {
                request.param("sku", sku);
            }
            request = withRoleAuth(request);
            if (hasRoleAuth()) {
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

    @Quando("eu consultar o insumo por id")
    public void getSupplyById() {
        executeWithOptionalAuth("GET", "/supplies/" + supplyId, null);
    }

    @Quando("eu criar o insumo")
    public void createSupply() {
        executeWithOptionalAuth("POST", "/supplies", requestBody);
    }

    @Quando("eu atualizar o insumo")
    public void updateSupply() {
        executeWithOptionalAuth("PATCH", "/supplies/" + supplyId, requestBody);
    }

    @Quando("eu remover o insumo")
    public void deleteSupply() {
        executeWithOptionalAuth("DELETE", "/supplies/" + supplyId, null);
    }

    @Então("a resposta deve ter status {int}")
    public void verifyStatus(int status) {
        assertEquals(status, context.getLastStatusCode(), "Body: " + context.getLastResponseBodyAsString());
    }

    @Então("a resposta deve conter o campo {string}")
    public void verifyFieldExists(String field) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has(field), "Campo ausente: " + field + " Body: " + context.getLastResponseBodyAsString());
    }

    @Então("a resposta deve conter uma lista de dados")
    public void verifyDataArray() throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");
    }

    @Então("a resposta deve conter no maximo {int} insumos")
    public void verifyMaxDataSize(int maxSize) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");
        assertTrue(root.get("data").size() <= maxSize,
                "Quantidade de insumos acima do esperado: " + root.get("data").size());
    }

    @Então("a resposta deve conter ao menos um insumo com sku {string}")
    public void verifyHasSupplyWithSku(String expectedSku) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");

        boolean found = false;
        for (JsonNode item : root.get("data")) {
            if (item.has("sku") && expectedSku.equals(item.get("sku").asText())) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Nenhum insumo com sku encontrado: " + expectedSku);
    }

    @Então("o primeiro insumo retornado deve ter id maior que {long}")
    public void verifyFirstSupplyIdGreaterThan(long minId) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("data"), "Campo data ausente");
        assertTrue(root.get("data").isArray(), "Campo data nao eh array");
        assertFalse(root.get("data").isEmpty(), "Lista de insumos vazia");
        assertTrue(root.get("data").get(0).has("id"), "Primeiro item sem id");
        assertTrue(root.get("data").get(0).get("id").asLong() > minId,
                "Id do primeiro insumo nao respeita cursor");
    }

    @Então("a resposta deve conter o campo reason com valor {string}")
    public void verifyReason(String reason) throws Exception {
        JsonNode root = context.getLastResponseBody();
        assertTrue(root.has("reason"), "Campo reason ausente");
        assertEquals(reason, root.get("reason").asText());
    }

    @Então("a resposta deve refletir o payload enviado")
    public void verifyPayloadValues() throws Exception {
        JsonNode request = objectMapper.readTree(requestBody);
        JsonNode response = context.getLastResponseBody();

        for (String fieldName : iterable(request.fieldNames())) {
            JsonNode expectedValue = request.get(fieldName);
            assertTrue(response.has(fieldName), "Campo ausente na resposta: " + fieldName);
            assertEquals(expectedValue.asText(), response.get(fieldName).asText(),
                    "Valor divergente para campo: " + fieldName);
        }
    }

    private void executeWithOptionalAuth(String method, String url, String body) {
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
        } catch (Exception e) {
            context.setLastStatusCode(0);
            context.setLastResponseBody(e.getMessage());
        }
    }

    private static <T> Iterable<T> iterable(java.util.Iterator<T> iterator) {
        return () -> iterator;
    }
}
