package br.com.fiap.postech.domain.serviceorder.usecase;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.owner.model.Owner;
import br.com.fiap.postech.domain.serviceorder.exception.NoMatchingServiceOrdersException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderClientNotFoundException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderNotFoundException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderVehicleNotFoundException;
import br.com.fiap.postech.domain.serviceorder.model.ServiceOrder;
import br.com.fiap.postech.domain.vehicle.model.Vehicle;
import br.com.fiap.postech.port.persistence.owner.OwnerPersistencePort;
import br.com.fiap.postech.port.persistence.serviceorder.ServiceOrderPersistencePort;
import br.com.fiap.postech.port.persistence.vehicle.VehiclePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceOrderUseCaseTest {

    @Mock
    private ServiceOrderPersistencePort persistencePort;

    @Mock
    private OwnerPersistencePort ownerPersistencePort;

    @Mock
    private VehiclePersistencePort vehiclePersistencePort;

    @InjectMocks
    private ServiceOrderUseCase useCase;

    private ServiceOrder order;

    @BeforeEach
    void setUp() {
        order = mock(ServiceOrder.class);
    }

    // === scroll ===

    @Test
    void should_return_scroll_results() {
        ScrollPage<ServiceOrder> page = ScrollPage.<ServiceOrder>builder()
                .data(List.of(order))
                .isLast(true)
                .cursor(null)
                .pageSize(10)
                .build();

        when(persistencePort.scroll(null, null, null, null, 10, null)).thenReturn(page);

        ScrollPage<ServiceOrder> result = useCase.scroll(null, null, null, null, null, 10, null);

        assertThat(result).isSameAs(page);
    }

    @Test
    void should_throw_when_scroll_returns_empty() {
        ScrollPage<ServiceOrder> empty = ScrollPage.<ServiceOrder>builder()
                .data(List.of()).isLast(true).cursor(null).pageSize(10).build();

        when(persistencePort.scroll(null, null, null, null, 10, null)).thenReturn(empty);

        assertThatThrownBy(() -> useCase.scroll(null, null, null, null, null, 10, null))
                .isInstanceOf(NoMatchingServiceOrdersException.class);
    }

    @Test
    void should_resolve_clientId_from_document_when_provided() {
        Owner owner = mock(Owner.class);
        when(owner.getId()).thenReturn(5L);
        when(ownerPersistencePort.findByDocument("12345678901")).thenReturn(Optional.of(owner));

        ScrollPage<ServiceOrder> page = ScrollPage.<ServiceOrder>builder()
                .data(List.of(order)).isLast(true).cursor(null).pageSize(10).build();

        when(persistencePort.scroll(null, null, 5L, null, 10, null)).thenReturn(page);

        ScrollPage<ServiceOrder> result = useCase.scroll(null, null, null, "12345678901", null, 10, null);

        assertThat(result).isSameAs(page);
    }

    @Test
    void should_return_no_results_when_document_not_found() {
        when(ownerPersistencePort.findByDocument("00000000000")).thenReturn(Optional.empty());

        ScrollPage<ServiceOrder> empty = ScrollPage.<ServiceOrder>builder()
                .data(List.of()).isLast(true).cursor(null).pageSize(10).build();

        when(persistencePort.scroll(null, null, -1L, null, 10, null)).thenReturn(empty);

        assertThatThrownBy(() -> useCase.scroll(null, null, null, "00000000000", null, 10, null))
                .isInstanceOf(NoMatchingServiceOrdersException.class);
    }

    // === getById ===

    @Test
    void should_return_order_by_id() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));

        ServiceOrder result = useCase.getById(1L);

        assertThat(result).isSameAs(order);
    }

    @Test
    void should_throw_when_order_not_found_by_id() {
        when(persistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getById(99L))
                .isInstanceOf(ServiceOrderNotFoundException.class)
                .hasMessage("Service order not found for id: 99");
    }

    // === create ===

    @Test
    void should_create_order() {
        when(order.getClientId()).thenReturn(1L);
        when(order.getVehicleId()).thenReturn(2L);
        when(ownerPersistencePort.findById(1L)).thenReturn(Optional.of(mock(Owner.class)));
        when(vehiclePersistencePort.findById(2L)).thenReturn(Optional.of(mock(Vehicle.class)));
        when(persistencePort.save(order)).thenReturn(order);

        ServiceOrder result = useCase.create(order);

        assertThat(result).isSameAs(order);
        verify(order).setStatus("PENDING");
        verify(persistencePort).save(order);
    }

    @Test
    void should_throw_when_client_not_found_on_create() {
        when(order.getClientId()).thenReturn(99L);
        when(ownerPersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.create(order))
                .isInstanceOf(ServiceOrderClientNotFoundException.class)
                .hasMessage("Client not found for id: 99");

        verify(persistencePort, never()).save(any());
    }

    @Test
    void should_throw_when_vehicle_not_found_on_create() {
        when(order.getClientId()).thenReturn(1L);
        when(order.getVehicleId()).thenReturn(99L);
        when(ownerPersistencePort.findById(1L)).thenReturn(Optional.of(mock(Owner.class)));
        when(vehiclePersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.create(order))
                .isInstanceOf(ServiceOrderVehicleNotFoundException.class)
                .hasMessage("Vehicle not found for id: 99");

        verify(persistencePort, never()).save(any());
    }

    // === update ===

    @Test
    void should_update_order() {
        when(order.getClientId()).thenReturn(1L);
        when(order.getVehicleId()).thenReturn(2L);
        when(ownerPersistencePort.findById(1L)).thenReturn(Optional.of(mock(Owner.class)));
        when(vehiclePersistencePort.findById(2L)).thenReturn(Optional.of(mock(Vehicle.class)));
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        ServiceOrder result = useCase.update(1L, order);

        assertThat(result).isSameAs(order);
        verify(order).setId(1L);
        verify(persistencePort).save(order);
    }

    @Test
    void should_throw_when_order_not_found_on_update() {
        when(order.getClientId()).thenReturn(1L);
        when(order.getVehicleId()).thenReturn(2L);
        when(ownerPersistencePort.findById(1L)).thenReturn(Optional.of(mock(Owner.class)));
        when(vehiclePersistencePort.findById(2L)).thenReturn(Optional.of(mock(Vehicle.class)));
        when(persistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.update(99L, order))
                .isInstanceOf(ServiceOrderNotFoundException.class);

        verify(persistencePort, never()).save(any());
    }

    @Test
    void should_throw_when_client_not_found_on_update() {
        when(order.getClientId()).thenReturn(99L);
        when(ownerPersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.update(1L, order))
                .isInstanceOf(ServiceOrderClientNotFoundException.class);

        verify(persistencePort, never()).save(any());
    }

    @Test
    void should_throw_when_vehicle_not_found_on_update() {
        when(order.getClientId()).thenReturn(1L);
        when(order.getVehicleId()).thenReturn(99L);
        when(ownerPersistencePort.findById(1L)).thenReturn(Optional.of(mock(Owner.class)));
        when(vehiclePersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.update(1L, order))
                .isInstanceOf(ServiceOrderVehicleNotFoundException.class);

        verify(persistencePort, never()).save(any());
    }

    // === delete ===

    @Test
    void should_delete_order() {
        when(persistencePort.existsById(1L)).thenReturn(true);

        useCase.delete(1L);

        verify(persistencePort).deleteById(1L);
    }

    @Test
    void should_throw_when_deleting_non_existing_order() {
        when(persistencePort.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> useCase.delete(99L))
                .isInstanceOf(ServiceOrderNotFoundException.class);

        verify(persistencePort, never()).deleteById(any());
    }

    // === registerProgress ===

    @Test
    void should_register_start_inspection() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerProgress(1L, "START_INSPECTION", null, null);

        verify(order).setStatus("IN_INSPECTION");
        verify(persistencePort).save(order);
    }

    @Test
    void should_register_complete_inspection() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerProgress(1L, "COMPLETE_INSPECTION", null, null);

        verify(order).setStatus("AWAITING_APPROVAL");
        verify(order).setInspectedAt(any());
    }

    @Test
    void should_register_start_service() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerProgress(1L, "START_SERVICE", "desc", 2L);

        verify(order).setStatus("IN_PROGRESS");
        verify(order).setStartedAt(any());
    }

    @Test
    void should_register_complete_service() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerProgress(1L, "COMPLETE_SERVICE", null, 1L);

        verify(order).setStatus("COMPLETED");
        verify(order).setCompletedAt(any());
    }

    @Test
    void should_register_cancel_service() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerProgress(1L, "CANCEL_SERVICE", null, 1L);

        verify(order).setStatus("CANCELLED");
        verify(order).setCancelledAt(any());
    }

    @Test
    void should_register_deliver_vehicle() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerProgress(1L, "DELIVER_VEHICLE", null, null);

        verify(order).setStatus("DELIVERED");
        verify(order).setDeliveredAt(any());
    }

    @Test
    void should_throw_when_order_not_found_on_progress() {
        when(persistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.registerProgress(99L, "START_INSPECTION", null, null))
                .isInstanceOf(ServiceOrderNotFoundException.class);

        verify(persistencePort, never()).save(any());
    }

    // === registerBudgetDecision ===

    @Test
    void should_register_approve_decision() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerBudgetDecision(1L, "APPROVE", "ok", List.of());

        verify(order).setStatus("APPROVED");
        verify(order).setApprovedAt(any());
    }

    @Test
    void should_register_cancel_decision() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerBudgetDecision(1L, "CANCEL", "nope", List.of());

        verify(order).setStatus("CANCELLED");
        verify(order).setCancelledAt(any());
    }

    @Test
    void should_register_reject_decision() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerBudgetDecision(1L, "REJECT", "too expensive", List.of());

        verify(order).setStatus("CANCELLED");
        verify(order).setRejectedAt(any());
    }

    @Test
    void should_register_partially_reject_decision() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(persistencePort.save(order)).thenReturn(order);

        useCase.registerBudgetDecision(1L, "PARTIALLY_REJECT", "partial", List.of(2L));

        verify(order).setStatus("PARTIALLY_REJECTED");
        verify(order).setPartiallyRejectedAt(any());
    }

    @Test
    void should_throw_when_order_not_found_on_budget_decision() {
        when(persistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.registerBudgetDecision(99L, "APPROVE", null, List.of()))
                .isInstanceOf(ServiceOrderNotFoundException.class);

        verify(persistencePort, never()).save(any());
    }
}
