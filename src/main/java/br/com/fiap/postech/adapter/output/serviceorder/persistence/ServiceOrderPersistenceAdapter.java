package br.com.fiap.postech.adapter.output.serviceorder.persistence;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.Scroller;
import br.com.fiap.postech.adapter.output.serviceorder.persistence.entity.ServiceOrderEntity;
import br.com.fiap.postech.adapter.output.serviceorder.persistence.repository.ServiceOrderRepository;
import br.com.fiap.postech.domain.serviceorder.model.ServiceOrder;
import br.com.fiap.postech.port.persistence.serviceorder.ServiceOrderPersistencePort;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServiceOrderPersistenceAdapter implements ServiceOrderPersistencePort {

    private final ServiceOrderRepository repository;

    @Override
    public ScrollPage<ServiceOrder> scroll(String status, Long clientId, Long vehicleId, Integer pageSize, String cursor) {
        return Scroller.scroll(
                cursor,
                pageSize,
                (parsedCursor, pageable) -> repository.findAll(buildSpecification(status, clientId, vehicleId, parsedCursor), pageable)
                        .getContent()
                        .stream()
                        .map(item -> (ServiceOrder) item)
                        .toList()
        );
    }

    private Specification<ServiceOrderEntity> buildSpecification(String status, Long clientId, Long vehicleId, Long cursor) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            query.orderBy(criteriaBuilder.asc(root.get("id")));

            final var predicates = new ArrayList<Predicate>();

            if (status != null && !status.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (clientId != null) {
                predicates.add(criteriaBuilder.equal(root.get("clientId"), clientId));
            }

            if (vehicleId != null) {
                predicates.add(criteriaBuilder.equal(root.get("vehicleId"), vehicleId));
            }

            if (cursor > 0) {
                predicates.add(criteriaBuilder.greaterThan(root.get("id"), cursor));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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
                .statusLabel(serviceOrder.getStatusLabel())
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
