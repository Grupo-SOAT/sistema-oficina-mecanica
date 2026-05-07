package br.com.fiap.postech.domain.user;

import br.com.fiap.postech.domain.user.enums.Roles;
import br.com.fiap.postech.domain.user.exception.*;
import br.com.fiap.postech.domain.user.model.User;
import br.com.fiap.postech.domain.user.model.UserDTO;
import br.com.fiap.postech.port.user.UserPort;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserPort userPort;

    private UserUseCase useCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new UserUseCase(userPort);
    }

    @Test
    void shouldCreateUser() {
        UserDTO dto = new UserDTO("andre", List.of(Roles.ADMIN));

        when(userPort.findByUsername("andre")).thenReturn(Optional.empty());
        when(userPort.getDefaultPassword()).thenReturn("123");
        when(userPort.createUser(dto, "123"))
                .thenReturn(new User(1L, "andre", List.of(Roles.ADMIN)));

        User result = useCase.createUser(dto);

        assertEquals("andre", result.username());
    }

    @Test
    void shouldThrowWhenUsernameExists() {
        UserDTO dto = new UserDTO("andre", List.of(Roles.ADMIN));

        when(userPort.findByUsername("andre"))
                .thenReturn(Optional.of(mock(User.class)));

        assertThrows(SameUsernameException.class,
                () -> useCase.createUser(dto));
    }

    @Test
    void shouldDeleteUser() {
        when(userPort.findById(1L))
                .thenReturn(Optional.of(mock(User.class)));

        useCase.deleteUser(1L);

        verify(userPort).deleteUser(1L);
    }

    @Test
    void shouldThrowWhenDeleteUserNotFound() {
        when(userPort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> useCase.deleteUser(1L));
    }

    @Test
    void shouldGetUserById() {
        User user = new User(1L, "andre", List.of(Roles.ADMIN));

        when(userPort.findById(1L)).thenReturn(Optional.of(user));

        assertEquals(user, useCase.getUserById(1L));
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userPort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> useCase.getUserById(1L));
    }

    @Test
    void shouldUpdateUser() {
        UserDTO dto = new UserDTO("andre", List.of(Roles.ADMIN));
        User user = new User(1L, "andre", List.of(Roles.ADMIN));

        when(userPort.findById(1L)).thenReturn(Optional.of(user));
        when(userPort.updateUser(1L, dto)).thenReturn(1);
        when(userPort.findById(1L)).thenReturn(Optional.of(user));

        User result = useCase.updateUser(1L, dto);

        assertEquals(user, result);
    }

    @Test
    void shouldThrowWhenUpdateFails() {
        UserDTO dto = new UserDTO("andre", List.of(Roles.ADMIN));

        when(userPort.findById(1L))
                .thenReturn(Optional.of(mock(User.class)));
        when(userPort.updateUser(1L, dto)).thenReturn(0);

        assertThrows(UserNotFoundException.class,
                () -> useCase.updateUser(1L, dto));
    }

    @Test
    void shouldScrollUsers() {
        ScrollPage<User> page = mock(ScrollPage.class);
        when(page.data()).thenReturn(List.of(mock(User.class)));

        when(userPort.scroll(null, 10, "abc")).thenReturn(page);

        assertEquals(page, useCase.scroll(null, 10, "abc"));
    }

    @Test
    void shouldThrowWhenNoUsersFound() {
        ScrollPage<User> page = mock(ScrollPage.class);
        when(page.data()).thenReturn(List.of());

        when(userPort.scroll(null, 10, "abc")).thenReturn(page);

        assertThrows(NoMatchingUsersException.class,
                () -> useCase.scroll(null, 10, "abc"));
    }

    @Test
    void shouldResetPassword() {
        when(userPort.findById(1L))
                .thenReturn(Optional.of(mock(User.class)));

        useCase.resetUserPassoword(1L);

        verify(userPort).resetarSenhaUsuario(1L);
    }
}