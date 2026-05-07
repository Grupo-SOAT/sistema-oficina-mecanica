package br.com.fiap.postech.domain.service.usecase;

import br.com.fiap.postech.domain.catalogservices.exception.CatalogServiceNotFoundException;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.service.exception.NoMatchingServicesException;
import br.com.fiap.postech.domain.service.exception.ServiceNotFoundException;
import br.com.fiap.postech.domain.service.exception.reason.ServiceExceptionReason;
import br.com.fiap.postech.domain.service.model.NeededSupply;
import br.com.fiap.postech.domain.service.model.Service;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderNotFoundException;
import br.com.fiap.postech.port.persistence.catalogService.CatalogServicesPersistencePort;
import br.com.fiap.postech.port.persistence.service.ServicePersistencePort;
import br.com.fiap.postech.port.persistence.serviceorder.ServiceOrderPersistencePort;
import br.com.fiap.postech.port.persistence.supply.SupplyPersistencePort;

import java.math.BigDecimal;
import java.util.List;

public class ServiceUseCase {
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final ServicePersistencePort persistencePort;
    private final ServiceOrderPersistencePort serviceOrderPersistencePort;
    private final CatalogServicesPersistencePort catalogServicesPersistencePort;
    private final SupplyPersistencePort supplyPersistencePort;

    public ServiceUseCase(
            ServicePersistencePort persistencePort,
            ServiceOrderPersistencePort serviceOrderPersistencePort,
            CatalogServicesPersistencePort catalogServicesPersistencePort,
            SupplyPersistencePort supplyPersistencePort
    ) {
        this.persistencePort = persistencePort;
        this.serviceOrderPersistencePort = serviceOrderPersistencePort;
        this.catalogServicesPersistencePort = catalogServicesPersistencePort;
        this.supplyPersistencePort = supplyPersistencePort;
    }

    public ScrollPage<Service> scroll(Long serviceOrderId, Long serviceId, String name, Integer pageSize, String cursor) {
        ensureServiceOrderExists(serviceOrderId);
        final var result = persistencePort.scroll(serviceOrderId, serviceId, name, pageSize, cursor);

        if (result.data().isEmpty()) {
            throw new NoMatchingServicesException(serviceOrderId);
        }

        return result;
    }

    public Service getById(Long serviceOrderId, Long serviceId) {
        ensureServiceOrderExists(serviceOrderId);
        return persistencePort.findByIdAndServiceOrderId(serviceId, serviceOrderId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));
    }

    public Service create(Long serviceOrderId, Service service) {
        ensureServiceOrderExists(serviceOrderId);
        validateService(service);
        service.setServiceOrderId(serviceOrderId);
        service.setStatus("AWAITING_APPROVAL");
        return persistencePort.save(service);
    }

    public Service update(Long serviceOrderId, Long serviceId, Service service) {
        ensureServiceOrderExists(serviceOrderId);
        final var existing = persistencePort.findByIdAndServiceOrderId(serviceId, serviceOrderId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        validateService(service);
        service.setId(serviceId);
        service.setServiceOrderId(serviceOrderId);
        // IMPORTANTE: preservar status existente - mudanças de status SÓ via endpoints de progresso
        service.setStatus(existing.getStatus());
        return persistencePort.save(service);
    }

    public void delete(Long serviceOrderId, Long serviceId) {
        ensureServiceOrderExists(serviceOrderId);
        persistencePort.findByIdAndServiceOrderId(serviceId, serviceOrderId)
                .orElseThrow(() -> new ServiceNotFoundException(serviceId));

        persistencePort.deleteById(serviceId);
    }

    private void ensureServiceOrderExists(Long serviceOrderId) {
        if (serviceOrderPersistencePort.findById(serviceOrderId).isEmpty()) {
            throw new ServiceOrderNotFoundException(serviceOrderId);
        }
    }

    private void validateService(Service service) {
        if (service.getCatalogServiceId() == null || catalogServicesPersistencePort.findById(service.getCatalogServiceId()).isEmpty()) {
            throw new CatalogServiceNotFoundException(service.getCatalogServiceId());
        }

        if (service.getPrice() == null || service.getPrice().compareTo(ZERO) <= 0) {
            throw new InvalidServiceException(ServiceExceptionReason.INVALID_PRICE);
        }

        List<NeededSupply> neededSupplies = service.getNeededSupplies();
        if (neededSupplies == null) {
            return;
        }

        for (NeededSupply neededSupply : neededSupplies) {
            if (neededSupply.getQuantity() == null || neededSupply.getQuantity() <= 0) {
                throw new InvalidServiceException(ServiceExceptionReason.INVALID_NEEDED_SUPPLY_QUANTITY);
            }
            if (neededSupply.getIdSupply() == null || supplyPersistencePort.findById(neededSupply.getIdSupply().longValue()).isEmpty()) {
                throw new InvalidServiceException(ServiceExceptionReason.NEEDED_SUPPLY_NOT_FOUND);
            }
        }
    }

    public static class InvalidServiceException extends RuntimeException {
        public final ServiceExceptionReason reason;

        public InvalidServiceException(ServiceExceptionReason reason) {
            super(reason.name());
            this.reason = reason;
        }
    }
}
