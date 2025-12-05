package io.soffa.foundation.service.core.config;

import io.soffa.foundation.core.security.PlatformAuthManager;
import io.soffa.foundation.service.core.RequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final PlatformAuthManager authManager;
    private final String openApiAccess;

    public SecurityConfig(
        @Value("${app.openapi.access:permitAll}") String openApiAccess, PlatformAuthManager authManager
    ) {
        super();
        this.authManager = authManager;
        this.openApiAccess = openApiAccess;
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(requests -> {
                if ("authenticated".equalsIgnoreCase(openApiAccess)) {
                    requests.requestMatchers("/v3/**", "/swagger/*").authenticated();
                } else {
                    requests.requestMatchers("/v3/**", "/swagger/*").permitAll();
                }

                requests
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/**").permitAll()
                    .anyRequest()
                    .authenticated();
            })
            .addFilterBefore(new RequestFilter(authManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
