package br.com.fiap.postech.domain.service.usecase;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.service.exception.NoMatchingServicesException;
import br.com.fiap.postech.domain.service.exception.ServiceNotFoundException;
import br.com.fiap.postech.domain.service.model.Service;
import br.com.fiap.postech.port.persistence.service.ServicePersistencePort;

public class ServiceUseCase {
    private final ServicePersistencePort persistencePort;

    public ServiceUseCase(ServicePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public ScrollPage<Service> scroll(Long serviceOrderId, Long serviceId, String status, Integer pageSize, String cursor) {
        final var result = persistencePort.scroll(serviceOrderId, serviceId, status, pageSize, cursor);

        if (result.data().isEmpty()) {
            throw new NoMatchingServicesException(serviceOrderId);
        }

        return result;
    }

    public Service getById(Long serviceOrderId, Long serviceId) {
        return persistencePort.findByIdAndServiceOrderId(serviceId, serviceOrderId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));
    }

    public Service create(Long serviceOrderId, Service service) {
        service.setServiceOrderId(serviceOrderId);
        service.setStatus("AWAITING_APPROVAL");
        return persistencePort.save(service);
    }

    public Service update(Long serviceOrderId, Long serviceId, Service service) {
        persistencePort.findByIdAndServiceOrderId(serviceId, serviceOrderId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        service.setId(serviceId);
        service.setServiceOrderId(serviceOrderId);
        return persistencePort.save(service);
    }

    public void delete(Long serviceOrderId, Long serviceId) {
        persistencePort.findByIdAndServiceOrderId(serviceId, serviceOrderId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        persistencePort.deleteById(serviceId);
    }
}
