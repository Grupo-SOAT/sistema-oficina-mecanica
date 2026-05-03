package br.com.fiap.postech.domain.catalogservices.exception;

import br.com.fiap.postech.domain.catalogservices.exception.reason.CatalogServicesExceptionReason;

public class CatalogServicesNotFoundException extends RuntimeException {
    public CatalogServicesExceptionReason reason = CatalogServicesExceptionReason.CATALOG_SERVICES_NOT_FOUND;
    public CatalogServicesNotFoundException(Long id) {
        super("Catalog services not found for id: " + id);
    }
}
