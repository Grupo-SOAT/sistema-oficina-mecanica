package br.com.fiap.postech.domain.authentication;

import java.util.Optional;

import br.com.fiap.postech.domain.authentication.model.Authentication;
import br.com.fiap.postech.domain.authentication.model.UserLogin;
import br.com.fiap.postech.domain.user.exception.UsuarioNaoEncontradoPorUsernameException;
import br.com.fiap.postech.domain.user.model.User;
import br.com.fiap.postech.port.authentication.AuthenticationPort;
import br.com.fiap.postech.port.user.UserPort;

public class AuthenticationUseCase {

    private final AuthenticationPort authenticationPort;
    private final UserPort userPort;

    public AuthenticationUseCase(AuthenticationPort authenticationPort, UserPort userPort) {
        this.authenticationPort = authenticationPort;
        this.userPort = userPort;
    }

    public Authentication gerarTokenParaUsuario(UserLogin userLogin){

        Optional<User> usuario = userPort.encontrarUsuarioPorUsername(userLogin.username());

        if (!usuario.isPresent()){

            throw new UsuarioNaoEncontradoPorUsernameException("Usuário " + userLogin.username() + " não foi encontrado.");

        }
        
        User user = usuario.get();

        return authenticationPort.autenticar(user, userLogin);


    }
    
}
