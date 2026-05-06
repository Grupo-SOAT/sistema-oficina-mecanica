package br.com.fiap.postech.it.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class ServiceOrdersStepDefinitions extends FeatureStepSupport {
    private Long serviceOrderId;
    private Long serviceId;
    private String budgetDecision;
    private String budgetComment;
    private String budgetRejectedServiceIds;

    @Before
    public void initialize() {
        initializeFeature("service-orders", "services", "supplies");
        serviceOrderId = null;
        serviceId = null;
        budgetDecision = null;
        budgetComment = null;
        budgetRejectedServiceIds = null;
    }

    @Dado("que o filtro status seja {string}")
    public void setStatusFilter(String value) {
        context.setFilterName("status");
        context.setFilterValue(value);
    }

    @Dado("que o filtro clientDocument seja {string}")
    public void setClientDocumentFilter(String value) {
        context.setFilterName("clientDocument");
        context.setFilterValue(value);
    }

    @Dado("que o filtro name seja {string}")
    public void setNameFilter(String value) {
        context.setFilterName("name");
        context.setFilterValue(value);
    }

    @Dado("que o filtro serviceId seja {long}")
    public void setServiceIdFilter(Long value) {
        context.setFilterName("serviceId");
        context.setFilterValue(String.valueOf(value));
    }

    @Dado("que o id da ordem de serviço seja {long}")
    public void setServiceOrderId(Long id) {
        serviceOrderId = id;
    }

    @Dado("que o id do serviço da ordem de serviço seja {long}")
    public void setServiceId(Long id) {
        serviceId = id;
    }

    @Dado("que o corpo da nova ordem de serviço seja:")
    public void setCreateOrderBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Dado("que o corpo de atualização da ordem de serviço seja:")
    public void setUpdateOrderBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Dado("que o corpo do novo serviço da ordem de serviço seja:")
    public void setCreateServiceBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Dado("que o corpo de atualização do serviço da ordem de serviço seja:")
    public void setUpdateServiceBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Dado("que o corpo da ação de progresso da ordem de serviço seja:")
    public void setProgressBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Dado("que a decisão do cliente seja {string}")
    public void setBudgetDecision(String decision) {
        budgetDecision = decision;
    }

    @Dado("que a observação da decisão seja {string}")
    public void setBudgetComment(String comment) {
        budgetComment = comment;
    }

    @Dado("que os serviços rejeitados sejam {string}")
    public void setRejectedServiceIds(String rejectedServiceIds) {
        budgetRejectedServiceIds = rejectedServiceIds;
    }

    @Dado("que o orçamento da ordem de serviço de ID {string} foi enviado ao cliente")
    public void setBudgetServiceOrderId(String value) {
        serviceOrderId = Long.parseLong(value);
    }

    @Quando("eu listar as ordens de serviço")
    public void listServiceOrders() {
        executeRequest("GET", "/service-orders", null, paginationParams());
    }

    @Quando("eu consultar a ordem de serviço por id")
    public void getServiceOrderById() {
        executeRequest("GET", "/service-orders/" + serviceOrderId, null, null);
    }

    @Quando("eu criar a ordem de serviço")
    public void createServiceOrder() {
        executeRequest("POST", "/service-orders", context.getRequestBody(), null);
    }

    @Quando("eu atualizar a ordem de serviço")
    public void updateServiceOrder() {
        executeRequest("PATCH", "/service-orders/" + serviceOrderId, context.getRequestBody(), null);
    }

    @Quando("eu remover a ordem de serviço")
    public void deleteServiceOrder() {
        executeRequest("DELETE", "/service-orders/" + serviceOrderId, null, null);
    }

    @Quando("eu listar os serviços da ordem de serviço")
    public void listServices() {
        executeRequest("GET", "/service-orders/" + serviceOrderId + "/services", null, paginationParams());
    }

    @Quando("eu consultar o serviço da ordem de serviço por id")
    public void getServiceById() {
        executeRequest("GET", "/service-orders/" + serviceOrderId + "/services/" + serviceId, null, null);
    }

    @Quando("eu incluir o serviço na ordem de serviço")
    public void createService() {
        executeRequest("POST", "/service-orders/" + serviceOrderId + "/services", context.getRequestBody(), null);
    }

    @Quando("eu atualizar o serviço da ordem de serviço")
    public void updateService() {
        executeRequest("PATCH", "/service-orders/" + serviceOrderId + "/services/" + serviceId, context.getRequestBody(), null);
    }

    @Quando("eu remover o serviço da ordem de serviço")
    public void deleteService() {
        executeRequest("DELETE", "/service-orders/" + serviceOrderId + "/services/" + serviceId, null, null);
    }

    @Quando("eu registrar o progresso da ordem de serviço")
    public void registerProgress() {
        executeRequest("POST", "/service-orders/" + serviceOrderId + "/progress", context.getRequestBody(), null);
    }

    @Quando("eu registrar a decisão do cliente sobre o orçamento da ordem de serviço")
    public void registerBudgetDecision() throws Exception {
        if (context.getCurrentRole() == null || context.getCurrentRole().isBlank()) {
            performLoginAs("admin");
        }

        java.util.List<Long> rejectedIds = new java.util.ArrayList<>();
        if (budgetRejectedServiceIds != null && !budgetRejectedServiceIds.isBlank()) {
            for (String part : budgetRejectedServiceIds.split(",")) {
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    rejectedIds.add(Long.parseLong(trimmed));
                }
            }
        }

        java.util.Map<String, Object> bodyMap = new java.util.LinkedHashMap<>();
        bodyMap.put("decision", budgetDecision);
        bodyMap.put("comment", budgetComment);
        bodyMap.put("rejectedServiceIds", rejectedIds);

        String body = objectMapper.writeValueAsString(bodyMap);
        executeRequest("POST", "/service-orders/" + serviceOrderId + "/budget", body, null);
    }

    @Então("a resposta deve conter no maximo {int} ordens de serviço")
    public void verifyMaxOrders(int maxSize) throws Exception {
        assertMaxDataSize(maxSize, "ordens de serviço");
    }

    @Então("a resposta deve conter ao menos uma ordem de serviço com status {string}")
    public void verifyOrderStatus(String expectedStatus) throws Exception {
        assertHasItemWithField("status", expectedStatus, "ordem de serviço");
    }

    @Então("a resposta deve conter no maximo {int} serviços da ordem de serviço")
    public void verifyMaxServices(int maxSize) throws Exception {
        assertMaxDataSize(maxSize, "serviços da ordem de serviço");
    }

    @Então("a resposta deve conter ao menos um serviço da ordem de serviço com name {string}")
    public void verifyServiceName(String expectedName) throws Exception {
        assertHasItemWithField("name", expectedName, "serviço da ordem de serviço");
    }
}