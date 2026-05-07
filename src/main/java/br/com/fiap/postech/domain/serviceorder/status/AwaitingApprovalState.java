package br.com.fiap.postech.domain.serviceorder.status;

import br.com.fiap.postech.domain.serviceorder.model.ServiceOrderStatus;

import java.util.List;

/**
 * State representing an order waiting for client approval.
 */
public class AwaitingApprovalState extends ServiceOrderState {

    public AwaitingApprovalState() {
        super(ServiceOrderStatus.AWAITING_APPROVAL);
    }

    @Override
    public List<Object> transitionTo(ServiceOrderStatus targetStatus) {
        if (targetStatus == ServiceOrderStatus.APPROVED) {
            return List.of();
        }
        if (targetStatus == ServiceOrderStatus.CANCELLED) {
            return List.of();
        }
        if (targetStatus == ServiceOrderStatus.PARTIALLY_REJECTED) {
            return List.of(); // Allowed in machine, but V2 use case will return 501
        }
        return super.transitionTo(targetStatus);
    }
}
