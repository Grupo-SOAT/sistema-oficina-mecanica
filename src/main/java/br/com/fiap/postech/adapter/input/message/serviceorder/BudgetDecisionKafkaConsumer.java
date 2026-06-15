package br.com.fiap.postech.adapter.input.message.serviceorder;

import br.com.fiap.postech.adapter.input.api.model.BudgetDecision;
import br.com.fiap.postech.adapter.input.message.serviceorder.event.BudgetDecisionEvent;
import br.com.fiap.postech.port.serviceorder.ProcessBudgetDecisionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.budget.kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
public class BudgetDecisionKafkaConsumer {

    private final ProcessBudgetDecisionPort processBudgetDecisionPort;

    @Transactional
    @KafkaListener(topics = "budget-decision", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(BudgetDecisionEvent event) {
        processBudgetDecisionPort.process(event.getServiceOrderId(), BudgetDecision.valueOf(event.getDecision()));
    }
}
