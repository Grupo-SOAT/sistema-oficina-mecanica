package br.com.fiap.postech.domain.catalogServices.exception;

import br.com.fiap.postech.domain.catalogServices.exception.reason.CatalogServicesExceptionReason;

public class DuplicatedCatalogServicesException extends RuntimeException {
    public CatalogServicesExceptionReason reason = CatalogServicesExceptionReason.CATALOG_SERVICES_CONFLICT_DUPLICATED_NAME;
    public DuplicatedCatalogServicesException(String name) { super("Catalog services already exists for name" + name);
    }
}
