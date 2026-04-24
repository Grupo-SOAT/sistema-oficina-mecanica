package br.com.fiap.postech.it.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class CatalogServicesStepDefinitions extends FeatureStepSupport {
    private Long catalogServiceId;

    @Before
    public void initialize() {
        initializeFeature("catalog-services", "supplies");
        catalogServiceId = null;
    }

    @Dado("que o id do serviço catalogado seja {long}")
    public void setCatalogServiceId(Long id) {
        catalogServiceId = id;
    }

    @Dado("que o filtro id seja {long}")
    public void setIdFilter(Long value) {
        context.setFilterName("id");
        context.setFilterValue(String.valueOf(value));
    }

    @Dado("que o corpo do novo serviço catalogado seja:")
    public void setCreateBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Dado("que o corpo de atualização do serviço catalogado seja:")
    public void setUpdateBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Quando("eu listar os serviços catalogados")
    public void listCatalogServices() {
        executeRequest("GET", "/catalog-services", null, paginationParams());
    }

    @Quando("eu consultar o serviço catalogado por id")
    public void getCatalogServiceById() {
        executeRequest("GET", "/catalog-services/" + catalogServiceId, null, null);
    }

    @Quando("eu criar o serviço catalogado")
    public void createCatalogService() {
        executeRequest("POST", "/catalog-services", context.getRequestBody(), null);
    }

    @Quando("eu atualizar o serviço catalogado")
    public void updateCatalogService() {
        executeRequest("PATCH", "/catalog-services/" + catalogServiceId, context.getRequestBody(), null);
    }

    @Quando("eu remover o serviço catalogado")
    public void deleteCatalogService() {
        executeRequest("DELETE", "/catalog-services/" + catalogServiceId, null, null);
    }

    @Então("a resposta deve conter no maximo {int} serviços catalogados")
    public void verifyMaxCatalogServices(int maxSize) throws Exception {
        assertMaxDataSize(maxSize, "serviços catalogados");
    }

    @Então("a resposta deve conter ao menos um serviço catalogado com nome {string}")
    public void verifyCatalogServiceName(String expectedName) throws Exception {
        assertHasItemWithField("name", expectedName, "serviço catalogado");
    }
}