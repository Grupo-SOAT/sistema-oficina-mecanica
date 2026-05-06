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
    public ScrollPage<ServiceOrder> scroll(Long id, String status, Long clientId, Long vehicleId,
                                           Integer pageSize, String cursor) {
        return Scroller.scroll(
                cursor,
                pageSize,
                (parsedCursor, pageable) -> {
                    List<ServiceOrderEntity> results = repository.findAllWithFilters(
                            id, status, clientId, vehicleId, parsedCursor, pageable);
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
        ServiceOrderEntity entity;
        if (serviceOrder instanceof ServiceOrderEntity existing) {
            entity = existing;
        } else {
            entity = new ServiceOrderEntity();
            entity.setId(serviceOrder.getId());
            entity.setClientId(serviceOrder.getClientId());
            entity.setVehicleId(serviceOrder.getVehicleId());
            entity.setDescription(serviceOrder.getDescription());
            entity.setEstimatedAmount(serviceOrder.getEstimatedAmount());
            entity.setStatus(serviceOrder.getStatus());
            entity.setInspectedAt(serviceOrder.getInspectedAt());
            entity.setPartiallyRejectedAt(serviceOrder.getPartiallyRejectedAt());
            entity.setRejectedAt(serviceOrder.getRejectedAt());
            entity.setCancelledAt(serviceOrder.getCancelledAt());
            entity.setApprovedAt(serviceOrder.getApprovedAt());
            entity.setStartedAt(serviceOrder.getStartedAt());
            entity.setCompletedAt(serviceOrder.getCompletedAt());
            entity.setDeliveredAt(serviceOrder.getDeliveredAt());
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
