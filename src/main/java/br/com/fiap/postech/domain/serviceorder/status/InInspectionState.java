package br.com.fiap.postech.domain.serviceorder.status;

import br.com.fiap.postech.domain.serviceorder.model.ServiceOrderStatus;

import java.util.List;

/**
 * State representing an order under inspection.
 */
public class InInspectionState extends ServiceOrderState {

    public InInspectionState() {
        super(ServiceOrderStatus.IN_INSPECTION);
    }

    @Override
    public List<Object> transitionTo(ServiceOrderStatus targetStatus) {
        if (targetStatus == ServiceOrderStatus.AWAITING_APPROVAL) {
            return List.of();
        }
        if (targetStatus == ServiceOrderStatus.CANCELLED) {
            return List.of();
        }
        return super.transitionTo(targetStatus);
    }
}
