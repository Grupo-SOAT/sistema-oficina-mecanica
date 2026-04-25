package br.com.fiap.postech.adapter.output.user.exception;


public class SameUsernameException extends RuntimeException {
    public SameUsernameException(String message) {
        super(message);
    }
}
