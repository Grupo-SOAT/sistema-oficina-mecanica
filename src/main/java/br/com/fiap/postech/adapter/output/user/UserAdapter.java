package br.com.fiap.postech.adapter.output.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.adapter.output.user.persistence.entity.UserEntity;
import br.com.fiap.postech.adapter.output.user.persistence.repository.UserRepository;
import br.com.fiap.postech.domain.user.enums.Roles;
import br.com.fiap.postech.domain.user.model.User;
import br.com.fiap.postech.domain.user.model.UserDTO;
import br.com.fiap.postech.port.user.UserPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;

    @Value("${user.password.default}")
    private String defaultPassword;

    @Override
    public String getSenhaDefault() {
        return this.defaultPassword;
    }

    @Override
    public User encontrarUsuarioPorUsername(String username){

        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {

            return new User();

        }

        return new User(user.get().getId(), 
        user.get().getUsername(), rolesStringToRolesEnum(user.get().getRoles()));


    }

    @Override
    public User criarUsuario(UserDTO userDTO, String defaultPassword) {

        var entity = new UserEntity();

        entity.setUsername(userDTO.username());
        entity.setPassword(defaultPassword);
        entity.setRoles(rolesEnumToRolesString(userDTO.roles()));

        var savedUser = userRepository.save(entity);

        return new User(savedUser.getId(), 
        savedUser.getUsername(), 
        rolesStringToRolesEnum(savedUser.getRoles()));

    }

    private List<String> rolesEnumToRolesString(List<Roles> roles) {

        if (roles == null) {
            return null;
        }

        return roles.stream()
                .map(Roles::name)
                .toList();
    }

    private List<Roles> rolesStringToRolesEnum(List<String> roles) {

        if (roles == null) {
            return null;
        }

        return roles.stream()
                .map(Roles::valueOf)
                .toList();
    }

}
