package br.com.fiap.postech.config.domain;

import br.com.fiap.postech.domain.catalogservices.usecase.CatalogServicesUseCase;
import br.com.fiap.postech.domain.supply.usecase.SupplyUseCase;
import br.com.fiap.postech.port.persistence.catalogService.CatalogServicesPersistencePort;
import br.com.fiap.postech.domain.owner.usecase.OwnerUseCase;
import br.com.fiap.postech.domain.supply.usecase.SupplyUseCase;
import br.com.fiap.postech.domain.user.UserUseCase;
import br.com.fiap.postech.port.persistence.owner.OwnerPersistencePort;
import br.com.fiap.postech.domain.authentication.AuthenticationUseCase;
import br.com.fiap.postech.domain.reporting.model.CatalogServiceCalculatedAverageTime;
import br.com.fiap.postech.domain.reporting.usecase.CatalogServiceReportingUseCase;
import br.com.fiap.postech.port.authentication.AuthenticationPort;
import br.com.fiap.postech.port.persistence.supply.SupplyPersistencePort;
import br.com.fiap.postech.port.user.UserPort;
import br.com.fiap.postech.domain.vehicle.usecase.VehicleUseCase;
import br.com.fiap.postech.port.persistence.vehicle.VehiclePersistencePort;

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

    @Bean
    public CatalogServicesUseCase catalogServicesUseCase (CatalogServicesPersistencePort persistencePort){
        return new CatalogServicesUseCase(persistencePort);
    public UserUseCase userUseCase(UserPort userPort){
        return new UserUseCase(userPort);
    }

    @Bean
    public VehicleUseCase vehicleUseCase(
            VehiclePersistencePort persistencePort,
            OwnerPersistencePort ownerPersistencePort
    ) {
        return new VehicleUseCase(persistencePort, ownerPersistencePort);
    }

    @Bean
    public OwnerUseCase ownerUseCase(OwnerPersistencePort persistencePort) {
        return new OwnerUseCase(persistencePort);
    }

    @Bean
    public AuthenticationUseCase authenticationUseCase(
            AuthenticationPort authenticationPort,
            UserPort userPort
    ) {
        return new AuthenticationUseCase(authenticationPort, userPort);
    }
}
