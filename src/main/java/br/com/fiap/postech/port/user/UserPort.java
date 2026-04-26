package br.com.fiap.postech.port.user;

import java.util.Optional;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.user.model.User;
import br.com.fiap.postech.domain.user.model.UserDTO;

public interface UserPort {

    Optional<User> encontrarUsuarioPorUsername(String username);

    Optional<User> encontrarUsuarioPorId(Long id);

    User criarUsuario(UserDTO userDTO, String senhaDefault);

    void deletarUsuario(Long id);

    int atualizarUsuario(Long id, UserDTO userDTO);

    ScrollPage<User> scroll(String username, Integer pageSize, String cursor);

    void resetarSenhaUsuario(Long id);

    String getSenhaDefault();
}
