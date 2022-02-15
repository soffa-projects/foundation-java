package io.soffa.foundation.service.config;

import io.soffa.foundation.config.AppConfig;
import io.soffa.foundation.data.DB;
import io.soffa.foundation.data.TenantsLoader;
import io.soffa.foundation.service.data.DBImpl;
import io.soffa.foundation.service.state.DatabasePlane;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TenantsLoader createDefaultTenantsLoader() {
        return TenantsLoader.NOOP;
    }

    @Bean
    public DB createDB(AppConfig appConfig,
                       DatabasePlane dbState,
                       ApplicationContext context,
                       @Value("${spring.application.name}") String applicationName) {

        appConfig.configure();
        return new DBImpl(dbState, context, appConfig.getDb(), applicationName);
    }

    @Bean
    @Primary
    public DataSource createDatasource(DB db) {
        return (DataSource) db;
    }


}
