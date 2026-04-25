package br.com.fiap.postech.domain.user;

import br.com.fiap.postech.domain.user.exception.SameUsernameException;
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

        var usuario = userPort.encontrarUsuarioPorUsername(userDTO.username());

        if (usuario.id().equals(null) && usuario.username().equals(null)) {

            throw new SameUsernameException("Já existe um usuário com esse nome. Tente novamente com outro nome!");

        }

        return userPort.criarUsuario(userDTO, senhaDefault);

    }

    public void deletarUsuario(Long id){

        //userPort.deletarUsuario(id);

    }


    
}
