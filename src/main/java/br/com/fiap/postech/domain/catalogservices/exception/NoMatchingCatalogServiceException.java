package br.com.fiap.postech.domain.catalogservices.exception;

public class NoMatchingCatalogServiceException extends RuntimeException {
    public NoMatchingCatalogServiceException(String name) {
        super("No matching catalog service for name: " + name);
    }
}
