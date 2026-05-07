package br.com.fiap.postech.domain.serviceorder.model;

/**
 * Domain enum for ServiceOrder status.
 * Mirrors OpenAPI contract but without external dependency.
 */
public enum ServiceOrderStatus {
    PENDING,
    IN_INSPECTION,
    AWAITING_APPROVAL,
    PARTIALLY_REJECTED,
    CANCELLED,
    APPROVED,
    IN_PROGRESS,
    COMPLETED,
    DELIVERED
}
