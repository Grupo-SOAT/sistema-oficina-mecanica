package br.com.fiap.postech.domain.catalogServices.usecase;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.catalogServices.exception.CatalogServicesNotFoundException;
import br.com.fiap.postech.domain.catalogServices.exception.DuplicatedCatalogServicesException;
import br.com.fiap.postech.domain.catalogServices.exception.NoMatchingCatalogServiceException;
import br.com.fiap.postech.domain.catalogServices.model.CatalogServices;
import br.com.fiap.postech.domain.supply.exception.SupplyNotFoundException;
import br.com.fiap.postech.port.persistence.catalogService.CatalogServicesPersistencePort;

public class CatalogServicesUseCase {
    private final CatalogServicesPersistencePort persistencePort;

    public CatalogServicesUseCase(CatalogServicesPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public ScrollPage<CatalogServices> scroll(String name, Integer pageSize, String cursor) {
        final var result = persistencePort.scroll(name, pageSize, cursor);

        if (result.data().isEmpty()) {
            throw new NoMatchingCatalogServiceException(name);
        }

        return result;
    }

    public CatalogServices getById(Long id){
        return persistencePort.findById(id).orElseThrow(() -> new CatalogServicesNotFoundException(id));
    }

    public CatalogServices create(CatalogServices catalogServices) {
        return persistencePort.save(catalogServices);
    }

    public void delete(Long id) {
        if (!persistencePort.existsById(id)) {
            throw new CatalogServicesNotFoundException(id);
        }

        persistencePort.deleteById(id);
    }

    public CatalogServices update(Long id, CatalogServices catalogServices) {
        persistencePort.findByName(catalogServices.getName()).ifPresent(s -> {
            throw new DuplicatedCatalogServicesException(catalogServices.getName());
        });

        final var existing = persistencePort.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException(id));
        catalogServices.setCatalogServiceId(id);

        return persistencePort.save(catalogServices);
    }
}
