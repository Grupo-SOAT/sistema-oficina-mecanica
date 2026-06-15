package br.com.fiap.postech.domain.serviceorder.model;

import java.time.Instant;

public class BudgetApprovalToken {

    private Long id;
    private Long serviceOrderId;
    private String token;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant usedAt;

    public BudgetApprovalToken() {
    }

    public BudgetApprovalToken(Long serviceOrderId, String token, Instant expiresAt) {
        this.serviceOrderId = serviceOrderId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Instant usedAt) {
        this.usedAt = usedAt;
    }
}
