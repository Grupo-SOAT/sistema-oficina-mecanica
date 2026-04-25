package br.com.fiap.postech.domain.user;

import br.com.fiap.postech.domain.user.model.User;
import br.com.fiap.postech.domain.user.model.UserDTO;
import br.com.fiap.postech.port.user.UserPort;

public class UserUseCase {

    private final UserPort userPort;

    public UserUseCase(UserPort userPort) {
        this.userPort = userPort;
    }
    
    public User criarUsuario(UserDTO userDTO){

        // senha padrao quando o admin cria um usuario qualquer  
        String senhaDefault = userPort.getSenhaDefault();

        return userPort.criarUsuario(userDTO, senhaDefault);

    }


    
}
