package br.com.fiap.postech.config.domain;

import br.com.fiap.postech.domain.authentication.AuthenticationUseCase;
import br.com.fiap.postech.domain.catalogservices.usecase.CatalogServicesUseCase;
import br.com.fiap.postech.domain.owner.usecase.OwnerUseCase;
import br.com.fiap.postech.domain.reporting.model.ServiceCalculatedAverageTime;
import br.com.fiap.postech.domain.reporting.usecase.ServiceReportingUseCase;
import br.com.fiap.postech.domain.reporting.usecase.impl.ServiceReportingUseCaseImpl;
import br.com.fiap.postech.domain.service.usecase.ServiceUseCase;
import br.com.fiap.postech.domain.supply.usecase.SupplyUseCase;
import br.com.fiap.postech.domain.user.UserUseCase;
import br.com.fiap.postech.domain.vehicle.usecase.VehicleUseCase;
import br.com.fiap.postech.port.authentication.AuthenticationPort;
import br.com.fiap.postech.port.persistence.catalogService.CatalogServicesPersistencePort;
import br.com.fiap.postech.port.persistence.owner.OwnerPersistencePort;
import br.com.fiap.postech.port.persistence.service.ServicePersistencePort;
import br.com.fiap.postech.port.persistence.supply.SupplyPersistencePort;
import br.com.fiap.postech.port.persistence.vehicle.VehiclePersistencePort;
import br.com.fiap.postech.port.user.UserPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.LongStream;

@Configuration
public class UseCaseDependencyInjectionConfig {
    @Bean
    @ConditionalOnBooleanProperty(name = "config.api.mock.reports.average-time.enabled")
    public ServiceReportingUseCase catalogServiceReportingUseCaseMock() {
        class Mock implements ServiceReportingUseCase {
            private long randomTotalizer() {
                return ThreadLocalRandom.current().nextLong(0, 1001);
            }

            private double randomHours() {
                return ThreadLocalRandom.current().nextDouble(1.0, 72.0);
            }

            @Override
            public ServiceCalculatedAverageTime calculateAverageTime(Long catalogServiceId) {
                return ServiceCalculatedAverageTime.builder()
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
            public List<ServiceCalculatedAverageTime> calculateAverageTime() {
                return LongStream.rangeClosed(1, 100)
                        .mapToObj(this::calculateAverageTime)
                        .toList();
            }
        }

        return new Mock();
    }

    @Bean
    @ConditionalOnBooleanProperty(name = "config.api.mock.reports.average-time.enabled", havingValue = false, matchIfMissing = true)
    public ServiceReportingUseCase catalogServiceReportingUseCase(ServicePersistencePort persistencePort) {
        return new ServiceReportingUseCaseImpl(persistencePort);
    }

    @Bean
    public ServiceUseCase serviceUseCase(ServicePersistencePort persistencePort) {
        return new ServiceUseCase(persistencePort);
    }

    @Bean
    public SupplyUseCase supplyUseCase(SupplyPersistencePort persistencePort) {
        return new SupplyUseCase(persistencePort);
    }

    @Bean
    public CatalogServicesUseCase catalogServicesUseCase(CatalogServicesPersistencePort persistencePort) {
        return new CatalogServicesUseCase(persistencePort);
    }

    @Bean
    public UserUseCase userUseCase(UserPort userPort) {
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
