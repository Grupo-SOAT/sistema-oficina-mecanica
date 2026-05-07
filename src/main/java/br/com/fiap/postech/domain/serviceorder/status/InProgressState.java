package br.com.fiap.postech.domain.serviceorder.status;

import br.com.fiap.postech.domain.serviceorder.model.ServiceOrderStatus;

import java.util.List;

public class InProgressState extends ServiceOrderState {

    public InProgressState() {
        super(ServiceOrderStatus.IN_PROGRESS);
    }

    @Override
    public List<Object> transitionTo(ServiceOrderStatus targetStatus) {
        if (targetStatus == ServiceOrderStatus.COMPLETED) {
            return List.of();
        }
        if (targetStatus == ServiceOrderStatus.CANCELLED) {
            return List.of();
        }
        return super.transitionTo(targetStatus);
    }
}
