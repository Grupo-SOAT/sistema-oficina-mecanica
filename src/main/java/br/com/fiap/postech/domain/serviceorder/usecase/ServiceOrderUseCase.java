package br.com.fiap.postech.domain.serviceorder.usecase;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.serviceorder.exception.NoMatchingServiceOrdersException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderClientNotFoundException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderNotFoundException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderVehicleNotFoundException;
import br.com.fiap.postech.domain.serviceorder.model.ServiceOrder;
import br.com.fiap.postech.port.persistence.owner.OwnerPersistencePort;
import br.com.fiap.postech.port.persistence.serviceorder.ServiceOrderPersistencePort;
import br.com.fiap.postech.port.persistence.vehicle.VehiclePersistencePort;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceOrderUseCase {

    private final ServiceOrderPersistencePort persistencePort;
    private final OwnerPersistencePort ownerPersistencePort;
    private final VehiclePersistencePort vehiclePersistencePort;

    public ServiceOrderUseCase(
            ServiceOrderPersistencePort persistencePort,
            OwnerPersistencePort ownerPersistencePort,
            VehiclePersistencePort vehiclePersistencePort
    ) {
        this.persistencePort = persistencePort;
        this.ownerPersistencePort = ownerPersistencePort;
        this.vehiclePersistencePort = vehiclePersistencePort;
    }

    public ScrollPage<ServiceOrder> scroll(Long id, String status, Long clientId,
                                           String clientDocument, Long vehicleId,
                                           Integer pageSize, String cursor) {
        Long resolvedClientId = clientId;
        if (clientDocument != null && !clientDocument.isBlank()) {
            resolvedClientId = ownerPersistencePort.findByDocument(clientDocument)
                    .map(o -> o.getId())
                    .orElse(-1L);
        }

        final var result = persistencePort.scroll(id, status, resolvedClientId, vehicleId, pageSize, cursor);

        if (result.data().isEmpty()) {
            throw new NoMatchingServiceOrdersException();
        }

        return result;
    }

    public ServiceOrder getById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new ServiceOrderNotFoundException(id));
    }

    public ServiceOrder create(ServiceOrder serviceOrder) {
        validateClientExists(serviceOrder.getClientId());
        validateVehicleExists(serviceOrder.getVehicleId());

        serviceOrder.setStatus("PENDING");
        return persistencePort.save(serviceOrder);
    }

    public ServiceOrder update(Long id, ServiceOrder serviceOrder) {
        validateClientExists(serviceOrder.getClientId());
        validateVehicleExists(serviceOrder.getVehicleId());

        persistencePort.findById(id)
                .orElseThrow(() -> new ServiceOrderNotFoundException(id));

        serviceOrder.setId(id);
        return persistencePort.save(serviceOrder);
    }

    public void delete(Long id) {
        if (!persistencePort.existsById(id)) {
            throw new ServiceOrderNotFoundException(id);
        }
        persistencePort.deleteById(id);
    }

    public void registerProgress(Long id, String action, String additionalInfo, Long relatedServiceId) {
        ServiceOrder order = persistencePort.findById(id)
                .orElseThrow(() -> new ServiceOrderNotFoundException(id));

        LocalDateTime now = LocalDateTime.now();

        switch (action) {
            case "START_INSPECTION" -> order.setStatus("IN_INSPECTION");
            case "COMPLETE_INSPECTION" -> {
                order.setStatus("AWAITING_APPROVAL");
                order.setInspectedAt(now);
            }
            case "START_SERVICE" -> {
                order.setStatus("IN_PROGRESS");
                order.setStartedAt(now);
            }
            case "COMPLETE_SERVICE" -> {
                order.setStatus("COMPLETED");
                order.setCompletedAt(now);
            }
            case "CANCEL_SERVICE" -> {
                order.setStatus("CANCELLED");
                order.setCancelledAt(now);
            }
            case "DELIVER_VEHICLE" -> {
                order.setStatus("DELIVERED");
                order.setDeliveredAt(now);
            }
        }

        persistencePort.save(order);
    }

    public void registerBudgetDecision(Long id, String decision, String comment, List<Long> rejectedServiceIds) {
        ServiceOrder order = persistencePort.findById(id)
                .orElseThrow(() -> new ServiceOrderNotFoundException(id));

        LocalDateTime now = LocalDateTime.now();

        switch (decision) {
            case "APPROVE" -> {
                order.setStatus("APPROVED");
                order.setApprovedAt(now);
            }
            case "CANCEL" -> {
                order.setStatus("CANCELLED");
                order.setCancelledAt(now);
            }
            case "REJECT" -> {
                order.setStatus("CANCELLED");
                order.setRejectedAt(now);
            }
            case "PARTIALLY_REJECT" -> {
                order.setStatus("PARTIALLY_REJECTED");
                order.setPartiallyRejectedAt(now);
            }
        }

        persistencePort.save(order);
    }

    private void validateClientExists(Long clientId) {
        ownerPersistencePort.findById(clientId)
                .orElseThrow(() -> new ServiceOrderClientNotFoundException(clientId));
    }

    private void validateVehicleExists(Long vehicleId) {
        vehiclePersistencePort.findById(vehicleId)
                .orElseThrow(() -> new ServiceOrderVehicleNotFoundException(vehicleId));
    }
}
