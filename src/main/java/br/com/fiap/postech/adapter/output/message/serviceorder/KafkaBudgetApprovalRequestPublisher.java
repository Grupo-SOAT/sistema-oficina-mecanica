package br.com.fiap.postech.adapter.output.message.serviceorder;

import br.com.fiap.postech.port.message.serviceorder.BudgetApprovalRequestPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.budget.kafka.enabled", havingValue = "true")
@RequiredArgsConstructor
public class KafkaBudgetApprovalRequestPublisher implements BudgetApprovalRequestPublisherPort {

    private final KafkaTemplate<String, BudgetApprovalRequestEvent> kafkaTemplate;

    private static final String TOPIC = "budget-approval-request";

    @Override
    public void publish(Long serviceOrderId, String token) {
        BudgetApprovalRequestEvent event = new BudgetApprovalRequestEvent(serviceOrderId, token);
        kafkaTemplate.send(TOPIC, serviceOrderId.toString(), event);
    }

    public record BudgetApprovalRequestEvent(Long serviceOrderId, String token) {
    }
}
