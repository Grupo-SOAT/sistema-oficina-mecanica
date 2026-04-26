package br.com.fiap.postech.domain.catalogServices.exception;

import br.com.fiap.postech.domain.catalogServices.exception.reason.CatalogServicesExceptionReason;
import br.com.fiap.postech.domain.supply.exception.reason.SupplyExceptionReason;

public class CatalogServicesNotFoundException extends RuntimeException {
    public CatalogServicesExceptionReason reason = CatalogServicesExceptionReason.CATALOG_SERVICES_NOT_FOUND;
    public CatalogServicesNotFoundException(Long id) {
        super("Catalog services not found for id: " + id);
    }
}
