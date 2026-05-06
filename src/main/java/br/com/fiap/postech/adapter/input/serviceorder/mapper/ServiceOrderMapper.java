package br.com.fiap.postech.adapter.input.serviceorder.mapper;

import br.com.fiap.postech.adapter.input.api.model.PaginatedServiceOrderResponse;
import br.com.fiap.postech.adapter.input.api.model.ServiceOrderData;
import br.com.fiap.postech.adapter.input.api.model.ServiceOrderRequest;
import br.com.fiap.postech.adapter.input.api.model.ServiceOrderStatus;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.serviceorder.persistence.entity.ServiceOrderEntity;
import br.com.fiap.postech.domain.serviceorder.model.ServiceOrder;

import java.time.ZoneOffset;

public class ServiceOrderMapper {

    public static ServiceOrder fromApiRequest(ServiceOrderRequest request) {
        return ServiceOrderEntity.builder()
                .clientId(request.getClientId())
                .vehicleId(request.getVehicleId())
                .description(request.getDescription())
                .build();
    }

    public static ServiceOrder fromApiData(ServiceOrderData data) {
        ServiceOrderEntity entity = ServiceOrderEntity.builder()
                .id(data.getId())
                .clientId(data.getClientId())
                .vehicleId(data.getVehicleId())
                .description(data.getDescription())
                .estimatedAmount(data.getEstimatedAmount())
                .build();

        if (data.getStatus() != null) {
            entity.setStatus(data.getStatus().getValue());
        }
        if (data.getInspectedAt() != null) {
            entity.setInspectedAt(data.getInspectedAt().toLocalDateTime());
        }
        if (data.getPartiallyRejectedAt() != null) {
            entity.setPartiallyRejectedAt(data.getPartiallyRejectedAt().toLocalDateTime());
        }
        if (data.getRejectedAt() != null) {
            entity.setRejectedAt(data.getRejectedAt().toLocalDateTime());
        }
        if (data.getCancelledAt() != null) {
            entity.setCancelledAt(data.getCancelledAt().toLocalDateTime());
        }
        if (data.getApprovedAt() != null) {
            entity.setApprovedAt(data.getApprovedAt().toLocalDateTime());
        }
        if (data.getStartedAt() != null) {
            entity.setStartedAt(data.getStartedAt().toLocalDateTime());
        }
        if (data.getCompletedAt() != null) {
            entity.setCompletedAt(data.getCompletedAt().toLocalDateTime());
        }
        if (data.getDeliveredAt() != null) {
            entity.setDeliveredAt(data.getDeliveredAt().toLocalDateTime());
        }

        return entity;
    }

    public static ServiceOrderData toApiData(ServiceOrder order) {
        ServiceOrderData data = new ServiceOrderData()
                .id(order.getId())
                .clientId(order.getClientId())
                .vehicleId(order.getVehicleId())
                .description(order.getDescription())
                .estimatedAmount(order.getEstimatedAmount());

        if (order.getStatus() != null) {
            data.setStatus(ServiceOrderStatus.fromValue(order.getStatus()));
        }
        if (order.getCreatedAt() != null) {
            data.setCreatedAt(order.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getUpdatedAt() != null) {
            data.setUpdatedAt(order.getUpdatedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getInspectedAt() != null) {
            data.setInspectedAt(order.getInspectedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getPartiallyRejectedAt() != null) {
            data.setPartiallyRejectedAt(order.getPartiallyRejectedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getRejectedAt() != null) {
            data.setRejectedAt(order.getRejectedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getCancelledAt() != null) {
            data.setCancelledAt(order.getCancelledAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getApprovedAt() != null) {
            data.setApprovedAt(order.getApprovedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getStartedAt() != null) {
            data.setStartedAt(order.getStartedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getCompletedAt() != null) {
            data.setCompletedAt(order.getCompletedAt().atOffset(ZoneOffset.UTC));
        }
        if (order.getDeliveredAt() != null) {
            data.setDeliveredAt(order.getDeliveredAt().atOffset(ZoneOffset.UTC));
        }

        return data;
    }

    public static PaginatedServiceOrderResponse toPaginatedResponse(ScrollPage<ServiceOrder> page) {
        final var result = new PaginatedServiceOrderResponse()
                .pageSize(page.pageSize())
                .cursor(page.cursor())
                .isLast(page.isLast());

        page.data().forEach(item -> result.addDataItem(toApiData(item)));

        return result;
    }
}
