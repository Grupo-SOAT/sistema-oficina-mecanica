package br.com.fiap.postech.port.serviceorder;

import br.com.fiap.postech.adapter.input.api.model.BudgetDecision;

public interface ProcessBudgetDecisionPort {

    void process(Long serviceOrderId, BudgetDecision decision);
}
