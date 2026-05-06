package br.com.fiap.postech.adapter.input.serviceorder.controller;

import br.com.fiap.postech.adapter.input.api.model.BudgetDecisionRequest;
import br.com.fiap.postech.adapter.input.api.model.PaginatedServiceOrderResponse;
import br.com.fiap.postech.adapter.input.api.model.ServiceOrderActionRequest;
import br.com.fiap.postech.adapter.input.api.model.ServiceOrderData;
import br.com.fiap.postech.adapter.input.api.model.ServiceOrderRequest;
import br.com.fiap.postech.adapter.input.api.model.ServiceOrderStatus;
import br.com.fiap.postech.adapter.input.api.model.ErrorResponse;
import br.com.fiap.postech.adapter.input.serviceorder.mapper.ServiceOrderMapper;
import br.com.fiap.postech.domain.serviceorder.exception.NoMatchingServiceOrdersException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderClientNotFoundException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderNotFoundException;
import br.com.fiap.postech.domain.serviceorder.exception.ServiceOrderVehicleNotFoundException;
import br.com.fiap.postech.domain.serviceorder.usecase.ServiceOrderUseCase;
import br.com.fiap.postech.port.api.ServiceOrdersApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceOrdersController implements ServiceOrdersApi {

    private final ServiceOrderUseCase serviceOrderUseCase;

    @Override
    public ResponseEntity<PaginatedServiceOrderResponse> listServiceOrders(
            @Nullable Long id,
            @Nullable ServiceOrderStatus status,
            @Nullable Long clientId,
            @Nullable String clientDocument,
            @Nullable String clientPhone,
            @Nullable Long vehicleId,
            @Nullable String vehicleLicensePlate,
            Integer pageSize,
            @Nullable String cursor) {

        String statusValue = status != null ? status.getValue() : null;

        final var page = serviceOrderUseCase.scroll(
                id, statusValue, clientId, clientDocument, vehicleId, pageSize, cursor);

        return ResponseEntity.ok(ServiceOrderMapper.toPaginatedResponse(page));
    }

    @Override
    public ResponseEntity<ServiceOrderData> getServiceOrderById(Long id) {
        final var order = serviceOrderUseCase.getById(id);
        return ResponseEntity.ok(ServiceOrderMapper.toApiData(order));
    }

    @Override
    public ResponseEntity<ServiceOrderData> createServiceOrder(ServiceOrderRequest serviceOrderRequest) {
        final var newOrder = ServiceOrderMapper.fromApiRequest(serviceOrderRequest);
        final var created = serviceOrderUseCase.create(newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(ServiceOrderMapper.toApiData(created));
    }

    @Override
    public ResponseEntity<ServiceOrderData> updateServiceOrder(Long id, ServiceOrderData serviceOrderData) {
        final var order = ServiceOrderMapper.fromApiData(serviceOrderData);
        final var updated = serviceOrderUseCase.update(id, order);
        return ResponseEntity.ok(ServiceOrderMapper.toApiData(updated));
    }

    @Override
    public ResponseEntity<Void> deleteServiceOrder(Long id) {
        serviceOrderUseCase.delete(id);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> registerProgress(Long id, ServiceOrderActionRequest serviceOrderActionRequest) {
        serviceOrderUseCase.registerProgress(
                id,
                serviceOrderActionRequest.getAction().getValue(),
                serviceOrderActionRequest.getAdditionalInfo(),
                serviceOrderActionRequest.getRelatedServiceId()
        );
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> registerClientDecision(Long id, BudgetDecisionRequest budgetDecisionRequest) {
        List<Long> rejectedServiceIds = budgetDecisionRequest.getRejectedServiceIds();
        serviceOrderUseCase.registerBudgetDecision(
                id,
                budgetDecisionRequest.getDecision().getValue(),
                budgetDecisionRequest.getComment(),
                rejectedServiceIds
        );
        return ResponseEntity.accepted().build();
    }

    @ExceptionHandler(NoMatchingServiceOrdersException.class)
    public ResponseEntity<Void> handleNoMatching() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ServiceOrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ServiceOrderNotFoundException ex) {
        final var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), ex.reason.name(), ex.getMessage()));
    }

    @ExceptionHandler(ServiceOrderClientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClientNotFound(ServiceOrderClientNotFoundException ex) {
        final var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), ex.reason.name(), ex.getMessage()));
    }

    @ExceptionHandler(ServiceOrderVehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVehicleNotFound(ServiceOrderVehicleNotFoundException ex) {
        final var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), ex.reason.name(), ex.getMessage()));
    }
}
