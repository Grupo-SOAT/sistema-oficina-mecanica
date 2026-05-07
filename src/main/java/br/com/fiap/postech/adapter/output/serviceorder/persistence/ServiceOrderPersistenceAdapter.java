package br.com.fiap.postech.adapter.output.serviceorder.persistence;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.Scroller;
import br.com.fiap.postech.adapter.output.serviceorder.persistence.entity.ServiceOrderEntity;
import br.com.fiap.postech.adapter.output.serviceorder.persistence.repository.ServiceOrderRepository;
import br.com.fiap.postech.domain.serviceorder.model.ServiceOrder;
import br.com.fiap.postech.port.persistence.serviceorder.ServiceOrderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServiceOrderPersistenceAdapter implements ServiceOrderPersistencePort {

    private final ServiceOrderRepository repository;

    @Override
    public ScrollPage<ServiceOrder> scroll(String status, Integer pageSize, String cursor) {
        return Scroller.scroll(
                cursor,
                pageSize,
                (parsedCursor, pageable) -> {
                    boolean hasStatus = status != null && !status.isBlank();
                    List<ServiceOrderEntity> results;
                    if (hasStatus) {
                        results = repository.findAllByStatusAndIdGreaterThanOrderByIdAsc(status, parsedCursor, pageable);
                    } else {
                        results = repository.findAllByIdGreaterThanOrderByIdAsc(parsedCursor, pageable);
                    }
                    return results.stream().map(item -> (ServiceOrder) item).toList();
                }
        );
    }

    @Override
    public Optional<ServiceOrder> findById(Long id) {
        return repository.findById(id).map(item -> (ServiceOrder) item);
    }

    @Override
    public ServiceOrder save(ServiceOrder serviceOrder) {
        if (serviceOrder instanceof ServiceOrderEntity entity) {
            return repository.save(entity);
        }
        return repository.save(toEntity(serviceOrder));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    private ServiceOrderEntity toEntity(ServiceOrder serviceOrder) {
        return ServiceOrderEntity.builder()
                .id(serviceOrder.getId())
                .clientId(serviceOrder.getClientId())
                .vehicleId(serviceOrder.getVehicleId())
                .description(serviceOrder.getDescription())
                .status(serviceOrder.getStatus())
                .estimatedAmount(serviceOrder.getEstimatedAmount())
                .createdAt(serviceOrder.getCreatedAt())
                .updatedAt(serviceOrder.getUpdatedAt())
                .inspectedAt(serviceOrder.getInspectedAt())
                .approvedAt(serviceOrder.getApprovedAt())
                .rejectedAt(serviceOrder.getRejectedAt())
                .cancelledAt(serviceOrder.getCancelledAt())
                .startedAt(serviceOrder.getStartedAt())
                .completedAt(serviceOrder.getCompletedAt())
                .deliveredAt(serviceOrder.getDeliveredAt())
                .partiallyRejectedAt(serviceOrder.getPartiallyRejectedAt())
                .build();
    }
}
