package br.com.fiap.postech.domain.user;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.user.exception.IdUsuarioInexistenteException;
import br.com.fiap.postech.domain.user.exception.NoMatchingUsersException;
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

        if (userPort.encontrarUsuarioPorUsername(userDTO.username()).isPresent()) {
            throw new SameUsernameException("Já existe um usuário com esse nome.");
        }

        String senhaDefault = userPort.getSenhaDefault();

        return userPort.criarUsuario(userDTO, senhaDefault);
    }

    public void deletarUsuario(Long id){

        userPort.encontrarUsuarioPorId(id)
            .orElseThrow(() -> 
                new IdUsuarioInexistenteException("Não existe nenhum usuário com o id selecionado.")
            );

        userPort.deletarUsuario(id);
    }

    public User obterUsuarioPorId(Long id){

        return userPort.encontrarUsuarioPorId(id)
            .orElseThrow(() -> 
                new IdUsuarioInexistenteException("Não existe nenhum usuário com o id selecionado.")
            );
    }

    public User atualizarUsuarioPorId(Long id, UserDTO userDTO){

        userPort.encontrarUsuarioPorId(id)
            .orElseThrow(() -> 
                new IdUsuarioInexistenteException("Não existe nenhum usuário com o id selecionado.")
            );

        int updated = userPort.atualizarUsuario(id, userDTO);

        if (updated == 0) {
            throw new IdUsuarioInexistenteException("Não foi possível atualizar o usuário.");
        }

        return userPort.encontrarUsuarioPorId(id)
            .orElseThrow(); 
    }

    public ScrollPage<User> scroll(String username, Integer size, String cursor) {
        
        final var result = userPort.scroll( username, size, cursor);

        if (result.data().isEmpty()) {
            throw new NoMatchingUsersException(username);
        }

        return result;
    }
}