package br.com.fiap.postech.config.domain;

import br.com.fiap.postech.domain.reporting.model.CatalogServiceCalculatedAverageTime;
import br.com.fiap.postech.domain.reporting.usecase.CatalogServiceReportingUseCase;
import br.com.fiap.postech.domain.supply.usecase.SupplyUseCase;
import br.com.fiap.postech.port.persistence.supply.SupplyPersistencePort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

@Configuration
public class UseCaseDependencyInjectionConfig {
    @Bean
    @Primary
    @ConditionalOnBooleanProperty(name = "config.api.mock.reports.average-time.enabled")
    public CatalogServiceReportingUseCase catalogServiceReportingUseCase() {
        class Mock implements CatalogServiceReportingUseCase {
            private long randomTotalizer() {
                return ThreadLocalRandom.current().nextLong(0, 1001);
            }

            private double randomHours() {
                return ThreadLocalRandom.current().nextDouble(1.0, 72.0);
            }

            @Override
            public CatalogServiceCalculatedAverageTime calculateAverageTime(Long catalogServiceId) {
                return CatalogServiceCalculatedAverageTime.builder()
                        .id(catalogServiceId)
                        .name("Serviço Mockado")
                        .totalCreated(randomTotalizer())
                        .totalCompleted(randomTotalizer())
                        .averageTimeBetweenCreateAndComplete(randomHours())
                        .averageTimeBetweenStartAndComplete(randomHours())
                        .averageTimeBetweenApproveAndComplete(randomHours())
                        .averageTimeAwaitingBudgetApproval(randomHours())
                        .build();
            }

            @Override
            public List<CatalogServiceCalculatedAverageTime> calculateAverageTime() {
                return LongStream.rangeClosed(1, 100)
                        .mapToObj(this::calculateAverageTime)
                        .toList();
            }
        }

        return new Mock();
    }

    @Bean
    public SupplyUseCase supplyUseCase(SupplyPersistencePort persistencePort) {
        return new SupplyUseCase(persistencePort);
    }
}
