package br.com.fiap.postech.it.cucumber.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("it")
@ContextConfiguration(initializers = br.com.fiap.postech.config.PostgresContainerInitializer.class)
public class CucumberTestConfiguration {
}
