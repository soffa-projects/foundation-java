package io.soffa.foundation.spring.config;

import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.config.AppConfig;
import io.soffa.foundation.data.DefaultTenantsProvider;
import io.soffa.foundation.data.NoTenantsProvider;
import io.soffa.foundation.data.TenantsProvider;
import io.soffa.foundation.spring.data.MockDataSource;
import io.soffa.foundation.spring.data.TenantAwareDatasourceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    private static final Logger LOG = Logger.get(DBConfiguration.class);


    @Bean
    @ConditionalOnMissingBean
    public TenantsProvider createDefaultTenantsProvider(AppConfig appConfig) {
        if (!appConfig.hasDataSources()) {
            return new NoTenantsProvider();
        }
        return new DefaultTenantsProvider(appConfig.getDb().getDatasources().keySet());
    }

    @Bean
    @Primary
    public DataSource createDatasource(AppConfig appConfig, TenantsProvider tenantsProvider,
                                       @Value("${spring.application.name}") String applicationName) {
        if (!appConfig.hasDataSources()) {
            LOG.info("No datasources configured for this service.");
            return new MockDataSource();
        }

        TenantAwareDatasourceImpl ds = new TenantAwareDatasourceImpl(tenantsProvider, appConfig.getDb(), applicationName);
        ds.configure();
        return ds;
    }


}