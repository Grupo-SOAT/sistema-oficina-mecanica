package br.com.fiap.postech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Environment environment;

    public SecurityConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        boolean isMockProfile = environment.matchesProfiles("mock");

        if (isMockProfile) {
            return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .build();
        }

        return http.authorizeHttpRequests(auth -> auth
                        // SWAGGER DOCS
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html"
                        ).permitAll()

                        // Login
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/login",
                                "/auth/chatbot",
                                "/auth/change-password"
                        ).permitAll()

                        // Outras rotas
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }

}
