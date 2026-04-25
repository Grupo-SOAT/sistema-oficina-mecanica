package br.com.fiap.postech.domain.authentication;

import br.com.fiap.postech.port.authentication.AuthenticationPort;

public class AuthenticationUseCase {

    private final AuthenticationPort authenticationPort;

    public AuthenticationUseCase(AuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }
    
}
