package br.com.fiap.postech.adapter.input.user;

import br.com.fiap.postech.adapter.input.api.model.OneTimePassword;
import br.com.fiap.postech.adapter.input.api.model.PaginatedUserResponse;
import br.com.fiap.postech.adapter.input.api.model.UserData;
import br.com.fiap.postech.adapter.input.api.model.UserRequest;
import br.com.fiap.postech.port.api.UsersApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController implements UsersApi {

    @Override
    public ResponseEntity<UserData> createUser(UserRequest request) {

        // Mock de resposta
        UserData response = new UserData();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {

        // lógica aqui

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity<UserData> getUserById(Long id) {

        // validação da API key aqui

        UserData response = new UserData();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PaginatedUserResponse> listUsers(Long id,
        String username,
        Integer size,
        String cursor
    ) {

        // validação da API key aqui

        PaginatedUserResponse response = new PaginatedUserResponse();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<OneTimePassword> resetPassword(Long id) {

        // validação da API key aqui

        OneTimePassword response = new OneTimePassword();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserData> updateUser(Long id, UserData userData) {

        // validação da API key aqui

        UserData response = new UserData();

        return ResponseEntity.ok(response);
    }



}
