package br.com.fiap.postech.domain.serviceorder.status;

import br.com.fiap.postech.domain.serviceorder.model.ServiceOrderStatus;

import java.util.List;

public class ApprovedState extends ServiceOrderState {

    public ApprovedState() {
        super(ServiceOrderStatus.APPROVED);
    }

    @Override
    public List<Object> transitionTo(ServiceOrderStatus targetStatus) {
        if (targetStatus == ServiceOrderStatus.IN_PROGRESS) {
            return List.of();
        }
        if (targetStatus == ServiceOrderStatus.CANCELLED) {
            return List.of();
        }
        return super.transitionTo(targetStatus);
    }
}
