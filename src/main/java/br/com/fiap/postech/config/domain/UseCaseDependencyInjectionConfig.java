package br.com.fiap.postech.config.domain;

import br.com.fiap.postech.domain.service.usecase.ServiceUseCase;
import br.com.fiap.postech.port.persistence.service.ServicePersistencePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseDependencyInjectionConfig {

    @Bean
    public ServiceUseCase serviceUseCase(ServicePersistencePort persistencePort) {
        return new ServiceUseCase(persistencePort);
    }
}
