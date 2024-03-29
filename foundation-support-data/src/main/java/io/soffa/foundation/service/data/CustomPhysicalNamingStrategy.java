package io.soffa.foundation.service.data;

import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.core.AppConfig;
import lombok.AllArgsConstructor;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    private final AppConfig appConfig;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment env) {
        appConfig.configure();
        if (TextUtil.isEmpty(appConfig.getDb().getTablesPrefix())) {
            return name;
        }
        String tableName = appConfig.getDb().getTablesPrefix() + name;
        return new Identifier(tableName.toLowerCase(), false);
    }


}
