package br.com.fiap.postech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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


@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SameUsernameException.class)
    public ResponseEntity<Object> handleSameUsernameException(SameUsernameException ex) {
        
        var response = new ErrorResponse(400, "USER_ALREADY_EXISTS" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdUsuarioInexistenteException.class)
    public ResponseEntity<Object> handleIdUsuarioInexistenteException(IdUsuarioInexistenteException ex) {
        
        var response = new ErrorResponse(400, "ID_NOT_FOUND" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoMatchingUsersException.class)
    public ResponseEntity<Object> handleNoMatchingUsersException(NoMatchingUsersException ex) {
        
        var response = new ErrorResponse(400, "USERNAME_NOT_FOUND_PAGINATION" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NoMatchingVehiclesException.class)
    public ResponseEntity<Object> handleNoMatchingVehiclesException(NoMatchingVehiclesException ex) {
        
        var response = new ErrorResponse(400, "VEHICLE_NOT_FOUND_PAGINATION" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatedVehicleException.class)
    public ResponseEntity<Object> handleDuplicatedVehicleException(DuplicatedVehicleException ex) {
        
        var response = new ErrorResponse(400, "VEHICLE_ALREADY_EXISTS" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Object> handleVehicleNotFoundException(VehicleNotFoundException ex) {
        
        var response = new ErrorResponse(400, "VEHICLE_NOT_FOUND" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoMatchingOwnersException.class)
    public ResponseEntity<Object> handleNoMatchingOwnersException(NoMatchingOwnersException ex) {
        
        var response = new ErrorResponse(400, "OWNER_NOT_FOUND_PAGINATION" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicatedOwnerException.class)
    public ResponseEntity<Object> handleDuplicatedOwnerException(DuplicatedOwnerException ex) {
        
        var response = new ErrorResponse(400, "OWNER_ALREADY_EXISTS" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<Object> handleOwnerNotFoundException(OwnerNotFoundException ex) {
        
        var response = new ErrorResponse(400, "OWNER_NOT_FOUND" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDocumentException.class)
    public ResponseEntity<Object> handleInvalidDocumentException(InvalidDocumentException ex) {
        
        var response = new ErrorResponse(400, "DOCUMENT_INVALID" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Object> handleInvalidEmailException(InvalidEmailException ex) {
        
        var response = new ErrorResponse(400, "EMAIL_INVALID" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidLicensePlateException.class)
    public ResponseEntity<Object> handleInvalidLicensePlateException(InvalidLicensePlateException ex) {
        
        var response = new ErrorResponse(400, "LICENSE_PLATE_INVALID" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
