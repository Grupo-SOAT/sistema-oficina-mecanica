package br.com.fiap.postech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.postech.adapter.input.api.model.ErrorResponse;
import br.com.fiap.postech.domain.authentication.exception.SenhaInvalidaException;
import br.com.fiap.postech.domain.user.exception.IdUsuarioInexistenteException;
import br.com.fiap.postech.domain.user.exception.NoMatchingUsersException;
import br.com.fiap.postech.domain.user.exception.SameUsernameException;
import br.com.fiap.postech.domain.user.exception.UsuarioNaoEncontradoPorUsernameException;


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

    @ExceptionHandler(UsuarioNaoEncontradoPorUsernameException.class)
    public ResponseEntity<Object> handleUsuarioNaoEncontradoPorUsernameException(UsuarioNaoEncontradoPorUsernameException ex) {
        
        var response = new ErrorResponse(400, "USERNAME_NOT_FOUND" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    public ResponseEntity<Object> handleSenhaInvalidaException(SenhaInvalidaException ex) {
        
        var response = new ErrorResponse(400, "SENHA_INVALIDA" , ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
