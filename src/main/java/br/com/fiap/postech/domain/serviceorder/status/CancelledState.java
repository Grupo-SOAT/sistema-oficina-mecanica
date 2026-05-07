package br.com.fiap.postech.domain.serviceorder.status;

import br.com.fiap.postech.domain.serviceorder.model.ServiceOrderStatus;

import java.util.List;

public class CancelledState extends ServiceOrderState {

    public CancelledState() {
        super(ServiceOrderStatus.CANCELLED);
    }

    @Override
    public List<Object> transitionTo(ServiceOrderStatus targetStatus) {
        if (targetStatus == ServiceOrderStatus.DELIVERED) {
            return List.of();
        }
        return super.transitionTo(targetStatus);
    }
}
