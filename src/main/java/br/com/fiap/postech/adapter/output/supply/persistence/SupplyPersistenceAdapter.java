package br.com.fiap.postech.adapter.output.supply.persistence;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.Scroller;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.supply.persistence.entity.SupplyEntity;
import br.com.fiap.postech.adapter.output.supply.persistence.repository.SupplyRepository;
import br.com.fiap.postech.domain.supply.model.Supply;
import br.com.fiap.postech.port.persistence.supply.SupplyPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SupplyPersistenceAdapter implements SupplyPersistencePort {
    private final SupplyRepository repository;

    @Override
    public ScrollPage<Supply> scroll(String sku, Integer pageSize, String cursor) {
        return Scroller.scroll(
                cursor,
                pageSize,
                (parsedCursor, pageable) -> {
                    List<SupplyEntity> results = (sku == null || sku.isBlank())
                            ? repository.findAllAfterCursor(parsedCursor, pageable)
                            : repository.findBySkuAfterCursor(sku, parsedCursor, pageable);

                    return results.stream().map(item -> (Supply) item).toList();
                }
        );
    }

    @Override
    public Optional<Supply> findById(Long id) {
        return repository.findById(id).map(item -> (Supply) item);
    }

    @Override
    public Optional<Supply> findBySku(String sku) {
        return repository.findBySku(sku).map(item -> (Supply) item);
    }

    @Override
    public Supply save(Supply supply) {
        SupplyEntity entity;
        if (supply instanceof SupplyEntity supplyEntity) {
            entity = supplyEntity;
        } else {
            entity = new SupplyEntity();
            entity.setId(supply.getId());
            entity.setSku(supply.getSku());
            entity.setName(supply.getName());
            entity.setDescription(supply.getDescription());
            entity.setUnitPrice(supply.getUnitPrice());
            entity.setSuppliedBy(supply.getSuppliedBy());
            entity.setReservedQuantity(supply.getReservedQuantity());
            entity.setAvailableQuantity(supply.getAvailableQuantity());
            entity.setCreatedAt(supply.getCreatedAt());
        }

        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
