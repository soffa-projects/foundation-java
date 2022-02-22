package io.soffa.foundation.service.core.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.core.AppConfig;
import io.soffa.foundation.core.Operation;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.context.DefaultRequestContext;
import io.soffa.foundation.core.metrics.MetricsRegistry;
import io.soffa.foundation.core.openapi.OpenApiBuilder;
import io.soffa.foundation.core.operation.OperationsMapping;
import io.soffa.foundation.core.security.AuthManager;
import io.soffa.foundation.errors.ErrorUtil;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Configuration
public class PlatformBeansFactory {

    @Bean
    public RestTemplate createDefaultRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OperationsMapping createOperationsMapping(Set<Operation<?, ?>> operations) {
        return new OperationsMapping(operations);
    }

    @Bean
    @ConditionalOnMissingBean(AuthManager.class)
    public AuthManager createDefaultAuthManager() {
        return new NoopAuthManager();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(@Value("${app.package}") String defaultPackage,
                                                             @Value("${spring.application.name}") String serviceId) {
        if (TextUtil.isEmpty(defaultPackage)) {
            Logger.setRelevantPackage(defaultPackage);
            ErrorUtil.setRelevantPackage(defaultPackage);
        }
        DefaultRequestContext.setServiceName(serviceId);
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "app")
    public AppConfig createAppConfig(@Value("${spring.application.name}") String applicationName) {
        DefaultRequestContext.setServiceName(applicationName);
        return new AppConfig(applicationName);
    }


    @Bean
    @Primary
    public HttpFirewall looseHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setUnsafeAllowAnyHttpMethod(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }


    @Bean
    public OpenAPI createOpenAPI(AppConfig appConfig) {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(RequestContext.class);

        OpenApiBuilder builder = new OpenApiBuilder(appConfig.getOpenapi());
        return builder.build();
    }

    @Bean
    @Primary
    public MetricsRegistry createMetricsRegistry(MeterRegistry registry) {
        return new MetricsRegistryImpl(registry);
    }


}
