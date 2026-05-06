package br.com.fiap.postech.domain.serviceorder.exception;

import br.com.fiap.postech.domain.serviceorder.exception.reason.ServiceOrderExceptionReason;

public class NoMatchingServiceOrdersException extends RuntimeException {
    public ServiceOrderExceptionReason reason = ServiceOrderExceptionReason.SERVICE_ORDER_NO_MATCHING;

    public NoMatchingServiceOrdersException() {
        super("No service orders found matching the given filters");
    }
}
