package br.com.fiap.postech.adapter.output.catalogService.persistence;

import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.CatalogServicesEntity;
import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.NeededSupplyEntity;
import br.com.fiap.postech.adapter.output.catalogService.persistence.repository.CatalogServicesRepository;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.Scroller;
import br.com.fiap.postech.domain.catalogServices.model.CatalogServices;
import br.com.fiap.postech.port.persistence.catalogService.CatalogServicesPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CatalogServicesPersistenceAdapter implements CatalogServicesPersistencePort {
    private final CatalogServicesRepository repository;

    @Override
    public ScrollPage<CatalogServices> scroll(String name, Integer pageSize, String cursor) {
        return Scroller.scroll(
                cursor,
                pageSize,
                (parsedCursor, pageable) -> {
                    List<CatalogServicesEntity> results = (name == null || name.isBlank())
                            ? repository.findAllAfterCursor(parsedCursor, pageable)
                            : repository.findByNameAfterCursor(name, parsedCursor, pageable);

                    return results.stream().map(item -> (CatalogServices) item).toList();
                }
        );
    }

    @Override
    public CatalogServices save(CatalogServices catalogServices) {
        CatalogServicesEntity entity;

        if (catalogServices instanceof CatalogServicesEntity catalogServicesEntity){
            entity = catalogServicesEntity;
        } else {
            entity = new CatalogServicesEntity();
            entity.setCatalogServiceId(catalogServices.getCatalogServiceId());
            entity.setName(catalogServices.getName().toUpperCase());
            entity.setDescription(catalogServices.getDescription());
            entity.setBasePrice(catalogServices.getBasePrice());
        }
        if (entity.getSupplies() != null) {
            for (NeededSupplyEntity supply : entity.getSupplies()) {
                supply.setCatalogServices(entity);
            }
        }
        return repository.save(entity);
    }

    @Override
    public Optional<CatalogServices> findByName(String name) {
        return repository.findByName(name).map(item -> (CatalogServices) item);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<CatalogServices> findById(Long id) {
        return repository.findWithSuppliesByCatalogServiceId(id).map(item -> (CatalogServices) item);
    }
}
