package br.com.fiap.postech.adapter.input.authentication;

import br.com.fiap.postech.adapter.input.api.model.ChangePasswordRequest;
import br.com.fiap.postech.adapter.input.api.model.LoginRequest;
import br.com.fiap.postech.adapter.input.api.model.TokenResponse;
import br.com.fiap.postech.port.api.AuthenticationApi;

import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthenticationController implements AuthenticationApi {

    @Override
    public ResponseEntity<TokenResponse> authenticate(LoginRequest request) {

        // Mock de resposta
        TokenResponse response = new TokenResponse("dummy-token", OffsetDateTime.parse("2026-04-24T15:30:00-03:00"));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest request) {

        // lógica aqui

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity<TokenResponse> authenticatedWithApiKey(String apiKey) {

        // validação da API key aqui

        TokenResponse response = new TokenResponse("dummy-token-chatbot", OffsetDateTime.parse("2026-04-24T15:30:00-03:00"));

        return ResponseEntity.ok(response);
    }

}
