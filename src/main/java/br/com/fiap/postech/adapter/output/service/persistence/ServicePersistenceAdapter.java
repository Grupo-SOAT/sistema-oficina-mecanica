package br.com.fiap.postech.adapter.output.service.persistence;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.Scroller;
import br.com.fiap.postech.adapter.output.service.persistence.entity.NeededSupplyEntity;
import br.com.fiap.postech.adapter.output.service.persistence.entity.ServiceEntity;
import br.com.fiap.postech.adapter.output.service.persistence.repository.ServiceRepository;
import br.com.fiap.postech.domain.service.model.Service;
import br.com.fiap.postech.port.persistence.service.ServicePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServicePersistenceAdapter implements ServicePersistencePort {

    private final ServiceRepository repository;

    @Override
    public ScrollPage<Service> scroll(Long serviceOrderId, Long serviceId, String status, Integer pageSize, String cursor) {
        return Scroller.scroll(
                cursor,
                pageSize,
                (parsedCursor, pageable) -> {
                    boolean hasServiceId = serviceId != null;
                    boolean hasStatus = status != null && !status.isBlank();

                    List<ServiceEntity> results;
                    if (hasServiceId && hasStatus) {
                        results = repository.findByServiceOrderIdAndServiceIdAndStatus(serviceOrderId, serviceId, status, parsedCursor, pageable);
                    } else if (hasServiceId) {
                        results = repository.findByServiceOrderIdAndServiceId(serviceOrderId, serviceId, parsedCursor, pageable);
                    } else if (hasStatus) {
                        results = repository.findByServiceOrderIdAndStatus(serviceOrderId, status, parsedCursor, pageable);
                    } else {
                        results = repository.findAllByServiceOrderId(serviceOrderId, parsedCursor, pageable);
                    }

                    return results.stream().map(item -> (Service) item).toList();
                }
        );
    }

    @Override
    public Optional<Service> findByIdAndServiceOrderId(Long id, Long serviceOrderId) {
        return repository.findByIdAndServiceOrderId(id, serviceOrderId).map(item -> (Service) item);
    }

    @Override
    public Service save(Service service) {
        if (service instanceof ServiceEntity entity) {
            return repository.save(entity);
        }
        return repository.save(toEntity(service));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private ServiceEntity toEntity(Service service) {
        List<NeededSupplyEntity> supplyEntities = service.getNeededSupplies() == null
                ? new ArrayList<>()
                : service.getNeededSupplies().stream()
                        .map(n -> NeededSupplyEntity.builder()
                                .idSupply(n.getIdSupply())
                                .note(n.getNote())
                                .quantity(n.getQuantity())
                                .build())
                        .collect(Collectors.toCollection(ArrayList::new));

        return ServiceEntity.builder()
                .id(service.getId())
                .serviceOrderId(service.getServiceOrderId())
                .catalogServiceId(service.getCatalogServiceId())
                .price(service.getPrice())
                .neededSupplyEntities(supplyEntities)
                .status(service.getStatus())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .rejectedAt(service.getRejectedAt())
                .cancelledAt(service.getCancelledAt())
                .approvedAt(service.getApprovedAt())
                .startedAt(service.getStartedAt())
                .completedAt(service.getCompletedAt())
                .build();
    }
}
