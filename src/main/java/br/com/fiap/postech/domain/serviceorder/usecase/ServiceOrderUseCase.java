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

    public ScrollPage<ServiceOrder> scroll(String status, Integer pageSize, String cursor) {
        final var result = persistencePort.scroll(status, pageSize, cursor);

        if (result.data().isEmpty()) {
            throw new NoMatchingServiceOrdersException(status != null ? "status=" + status : "all");
        }

        return result;
    }

    public ServiceOrder getById(Long id) {
        return persistencePort.findById(id)
                .orElseThrow(() -> new ServiceOrderNotFoundException(id));
    }

    public ServiceOrder create(ServiceOrder serviceOrder) {
        validateOwnerExists(serviceOrder.getClientId());
        validateVehicleExists(serviceOrder.getVehicleId());

        serviceOrder.setStatus("PENDING");
        return persistencePort.save(serviceOrder);
    }

    public ServiceOrder update(Long id, ServiceOrder serviceOrder) {
        validateOwnerExists(serviceOrder.getClientId());
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

    private void validateOwnerExists(Long ownerId) {
        ownerPersistencePort.findById(ownerId)
                .orElseThrow(() -> new ServiceOrderClientNotFoundException(ownerId));
    }

    private void validateVehicleExists(Long vehicleId) {
        vehiclePersistencePort.findById(vehicleId)
                .orElseThrow(() -> new ServiceOrderVehicleNotFoundException(vehicleId));
    }
}
