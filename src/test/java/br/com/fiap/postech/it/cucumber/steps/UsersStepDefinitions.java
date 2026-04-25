package br.com.fiap.postech.it.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

public class UsersStepDefinitions extends FeatureStepSupport {
    private Long userId;

    @Before
    public void initialize() {
        initializeFeature("users");
        userId = null;
    }

    @Dado("que o filtro username seja {string}")
    public void setUsernameFilter(String value) {
        context.setFilterName("username");
        context.setFilterValue(value);
    }

    @Dado("que o id do usuario seja {long}")
    public void setUserId(Long id) {
        userId = id;
    }

    @Dado("que o corpo do novo usuario seja:")
    public void setCreateBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Dado("que o corpo de atualização do usuario seja:")
    public void setUpdateBody(DataTable table) throws Exception {
        setJsonBody(table);
    }

    @Quando("eu listar os usuarios")
    public void listUsers() {
        executeRequest("GET", "/users", null, paginationParams());
    }

    @Quando("eu consultar o usuario por id")
    public void getUserById() {
        executeRequest("GET", "/users/" + userId, null, null);
    }

    @Quando("eu criar o usuario")
    public void createUser() {
        executeRequest("POST", "/users", context.getRequestBody(), null);
    }

    @Quando("eu atualizar o usuario")
    public void updateUser() {
        executeRequest("PATCH", "/users/" + userId, context.getRequestBody(), null);
    }

    @Quando("eu remover o usuario")
    public void deleteUser() {
        executeRequest("DELETE", "/users/" + userId, null, null);
    }

    @Então("a resposta deve conter no maximo {int} usuarios")
    public void verifyMaxUsers(int maxSize) throws Exception {
        assertMaxDataSize(maxSize, "usuarios");
    }

    @Então("a resposta deve conter ao menos um usuario com username {string}")
    public void verifyUsername(String expectedUsername) throws Exception {
        assertHasItemWithField("username", expectedUsername, "usuario");
    }
}