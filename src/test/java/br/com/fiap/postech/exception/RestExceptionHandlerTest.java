package br.com.fiap.postech.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.postech.adapter.input.api.model.ErrorResponse;
import br.com.fiap.postech.domain.owner.exception.DuplicatedOwnerException;
import br.com.fiap.postech.domain.owner.exception.InvalidDocumentException;
import br.com.fiap.postech.domain.owner.exception.InvalidEmailException;
import br.com.fiap.postech.domain.owner.exception.NoMatchingOwnersException;
import br.com.fiap.postech.domain.owner.exception.OwnerNotFoundException;
import br.com.fiap.postech.domain.user.exception.IdUsuarioInexistenteException;
import br.com.fiap.postech.domain.user.exception.NoMatchingUsersException;
import br.com.fiap.postech.domain.user.exception.SameUsernameException;
import br.com.fiap.postech.domain.vehicle.excecption.DuplicatedVehicleException;
import br.com.fiap.postech.domain.vehicle.excecption.InvalidLicensePlateException;
import br.com.fiap.postech.domain.vehicle.excecption.NoMatchingVehiclesException;
import br.com.fiap.postech.domain.vehicle.excecption.VehicleNotFoundException;

public class RestExceptionHandlerTest {

    private RestExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RestExceptionHandler();
    }

    @Test
    void should_handle_same_username_exception() {
        ResponseEntity<Object> response = handler.handleSameUsernameException(
                new SameUsernameException("Username already exists")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.getCode()).isEqualTo(400);
        assertThat(body.getReason()).isEqualTo("USER_ALREADY_EXISTS");
        assertThat(body.getMessage()).isEqualTo("Username already exists");
    }

    @Test
    void should_handle_id_usuario_inexistente_exception() {
        ResponseEntity<Object> response = handler.handleIdUsuarioInexistenteException(
                new IdUsuarioInexistenteException("User id not found")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getCode()).isEqualTo(400);
        assertThat(body.getReason()).isEqualTo("ID_NOT_FOUND");
        assertThat(body.getMessage()).isEqualTo("User id not found");
    }

    @Test
    void should_handle_no_matching_users_exception() {
        ResponseEntity<Object> response = handler.handleNoMatchingUsersException(
                new NoMatchingUsersException("john")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("USERNAME_NOT_FOUND_PAGINATION");
    }

    @Test
    void should_handle_no_matching_vehicles_exception() {
        ResponseEntity<Object> response = handler.handleNoMatchingVehiclesException(
                new NoMatchingVehiclesException("ABC1234")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("VEHICLE_NOT_FOUND_PAGINATION");
    }

    @Test
    void should_handle_duplicated_vehicle_exception() {
        ResponseEntity<Object> response = handler.handleDuplicatedVehicleException(
                new DuplicatedVehicleException("ABC1234")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("VEHICLE_ALREADY_EXISTS");
    }

    @Test
    void should_handle_vehicle_not_found_exception() {
        ResponseEntity<Object> response = handler.handleVehicleNotFoundException(
                new VehicleNotFoundException(1L)
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("VEHICLE_NOT_FOUND");
    }

    @Test
    void should_handle_no_matching_owners_exception() {
        ResponseEntity<Object> response = handler.handleNoMatchingOwnersException(
                new NoMatchingOwnersException("teste@email.com")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("OWNER_NOT_FOUND_PAGINATION");
    }

    @Test
    void should_handle_duplicated_owner_exception() {
        ResponseEntity<Object> response = handler.handleDuplicatedOwnerException(
                new DuplicatedOwnerException("31058167049")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("OWNER_ALREADY_EXISTS");
    }

    @Test
    void should_handle_owner_not_found_exception() {
        ResponseEntity<Object> response = handler.handleOwnerNotFoundException(
                new OwnerNotFoundException(1L)
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("OWNER_NOT_FOUND");
    }

    @Test
    void should_handle_invalid_document_exception() {
        ResponseEntity<Object> response = handler.handleInvalidDocumentException(
                new InvalidDocumentException("123")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("DOCUMENT_INVALID");
    }

    @Test
    void should_handle_invalid_email_exception() {
        ResponseEntity<Object> response = handler.handleInvalidEmailException(
                new InvalidEmailException("email-invalido")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("EMAIL_INVALID");
    }

    @Test
    void should_handle_invalid_license_plate_exception() {
        ResponseEntity<Object> response = handler.handleInvalidLicensePlateException(
                new InvalidLicensePlateException("AAA000")
        );

        ErrorResponse body = (ErrorResponse) response.getBody();

        assertThat(body.getReason()).isEqualTo("LICENSE_PLATE_INVALID");
    }
}
