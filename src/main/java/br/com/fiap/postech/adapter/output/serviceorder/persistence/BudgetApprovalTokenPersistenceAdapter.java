package br.com.fiap.postech.adapter.output.serviceorder.persistence;

import br.com.fiap.postech.adapter.output.serviceorder.persistence.entity.BudgetApprovalTokenEntity;
import br.com.fiap.postech.adapter.output.serviceorder.persistence.repository.BudgetApprovalTokenRepository;
import br.com.fiap.postech.domain.serviceorder.model.BudgetApprovalToken;
import br.com.fiap.postech.port.persistence.serviceorder.BudgetApprovalTokenPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BudgetApprovalTokenPersistenceAdapter implements BudgetApprovalTokenPersistencePort {

    private final BudgetApprovalTokenRepository repository;

    @Override
    public BudgetApprovalToken create(BudgetApprovalToken token) {
        BudgetApprovalTokenEntity entity = BudgetApprovalTokenEntity.builder()
                .serviceOrderId(token.getServiceOrderId())
                .token(token.getToken())
                .expiresAt(token.getExpiresAt())
                .createdAt(token.getCreatedAt())
                .build();
        BudgetApprovalTokenEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<BudgetApprovalToken> findByServiceOrderId(Long serviceOrderId) {
        return repository.findByServiceOrderId(serviceOrderId)
                .map(this::toDomain);
    }

    @Override
    public void markUsed(Long serviceOrderId) {
        repository.findByServiceOrderId(serviceOrderId).ifPresent(entity -> {
            entity.setUsedAt(java.time.Instant.now());
            repository.save(entity);
        });
    }

    private BudgetApprovalToken toDomain(BudgetApprovalTokenEntity entity) {
        BudgetApprovalToken token = new BudgetApprovalToken(
                entity.getServiceOrderId(),
                entity.getToken(),
                entity.getExpiresAt()
        );
        token.setId(entity.getId());
        token.setCreatedAt(entity.getCreatedAt());
        token.setUsedAt(entity.getUsedAt());
        return token;
    }
}
