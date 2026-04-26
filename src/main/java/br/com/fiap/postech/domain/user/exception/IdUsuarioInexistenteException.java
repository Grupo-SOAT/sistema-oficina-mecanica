package br.com.fiap.postech.domain.user.exception;


public class IdUsuarioInexistenteException extends RuntimeException {
    public IdUsuarioInexistenteException(String message) {
        super(message);
    }
}
