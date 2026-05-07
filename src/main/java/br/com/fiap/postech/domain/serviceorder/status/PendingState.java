package br.com.fiap.postech.domain.serviceorder.status;

import br.com.fiap.postech.domain.serviceorder.model.ServiceOrderStatus;

import java.util.List;

/**
 * Initial state: PENDING.
 * From here, the only allowed transition is PENDING -> IN_INSPECTION.
 */
public class PendingState extends ServiceOrderState {

    public PendingState() {
        super(ServiceOrderStatus.PENDING);
    }

    @Override
    public List<Object> transitionTo(ServiceOrderStatus targetStatus) {
        if (targetStatus == ServiceOrderStatus.IN_INSPECTION) {
            return List.of(); // No commands for this transition in V1
        }
        if (targetStatus == ServiceOrderStatus.CANCELLED) {
            return List.of(); // Cancellation is always allowed
        }
        return super.transitionTo(targetStatus);
    }
}
