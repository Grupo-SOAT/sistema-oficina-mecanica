package br.com.fiap.postech.port.user;

import br.com.fiap.postech.domain.user.model.User;
import br.com.fiap.postech.domain.user.model.UserDTO;

public interface UserPort {

    public User criarUsuario(UserDTO userDTO, String senhaDefault);

    public String getSenhaDefault();
    
}
