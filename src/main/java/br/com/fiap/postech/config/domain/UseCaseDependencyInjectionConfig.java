package br.com.fiap.postech.config.domain;

import br.com.fiap.postech.domain.supply.usecase.SupplyUseCase;
import br.com.fiap.postech.domain.user.UserUseCase;
import br.com.fiap.postech.port.persistence.supply.SupplyPersistencePort;
import br.com.fiap.postech.port.user.UserPort;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseDependencyInjectionConfig {
    @Bean
    public SupplyUseCase supplyUseCase(SupplyPersistencePort persistencePort) {
        return new SupplyUseCase(persistencePort);
    }

    @Bean
    public UserUseCase userUseCase(UserPort userPort){
        return new UserUseCase(userPort);
    }

}
