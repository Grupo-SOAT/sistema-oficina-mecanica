package br.com.fiap.postech.domain.serviceorder.usecase;

import br.com.fiap.postech.adapter.input.api.model.BudgetDecision;
import br.com.fiap.postech.port.persistence.serviceorder.BudgetApprovalTokenPersistencePort;
import br.com.fiap.postech.port.serviceorder.ProcessBudgetDecisionPort;

public class ProcessBudgetDecisionUseCase implements ProcessBudgetDecisionPort {

    private final ChangeServiceOrderStatusUseCase changeServiceOrderStatusUseCase;
    private final BudgetApprovalTokenPersistencePort budgetApprovalTokenPersistencePort;

    public ProcessBudgetDecisionUseCase(
            ChangeServiceOrderStatusUseCase changeServiceOrderStatusUseCase,
            BudgetApprovalTokenPersistencePort budgetApprovalTokenPersistencePort
    ) {
        this.changeServiceOrderStatusUseCase = changeServiceOrderStatusUseCase;
        this.budgetApprovalTokenPersistencePort = budgetApprovalTokenPersistencePort;
    }

    @Override
    public void process(Long serviceOrderId, BudgetDecision decision) {
        changeServiceOrderStatusUseCase.registerClientDecision(serviceOrderId, decision);
        budgetApprovalTokenPersistencePort.markUsed(serviceOrderId);
    }
}
