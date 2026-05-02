package br.com.fiap.postech.port.persistence.catalogService;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.catalogServices.model.CatalogServices;
import br.com.fiap.postech.domain.supply.model.Supply;

import java.util.Optional;

public interface CatalogServicesPersistencePort {
    ScrollPage<CatalogServices> scroll(String name, Integer pageSize, String cursor);

    CatalogServices save(CatalogServices catalogServices);

    Optional<CatalogServices> findByName(String name);

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<CatalogServices> findById(Long id);
}
