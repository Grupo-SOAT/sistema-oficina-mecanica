package br.com.fiap.postech.adapter.input.authentication;

import br.com.fiap.postech.adapter.input.api.model.ChangePasswordRequest;
import br.com.fiap.postech.adapter.input.api.model.LoginRequest;
import br.com.fiap.postech.adapter.input.api.model.TokenResponse;
import br.com.fiap.postech.adapter.input.authentication.mapper.AuthenticationMapper;
import br.com.fiap.postech.domain.authentication.AuthenticationUseCase;
import br.com.fiap.postech.port.api.AuthenticationApi;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationUseCase authenticationUseCase;
    private final AuthenticationMapper mapper;

    @Override
    public ResponseEntity<TokenResponse> authenticate(LoginRequest request) {

        System.out.println("Request para login recebida!");

        var domainResponse = authenticationUseCase.gerarTokenParaUsuario(mapper.toDomain(request));

        var clientResponse = mapper.toClientResponse(domainResponse);

        System.out.println("token gerado com sucesso.");

        return ResponseEntity.ok(clientResponse);
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
