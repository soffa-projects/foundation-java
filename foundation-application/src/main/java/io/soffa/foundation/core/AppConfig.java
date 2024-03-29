package io.soffa.foundation.core;

import io.soffa.foundation.commons.CollectionUtil;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.core.context.DefaultRequestContext;
import io.soffa.foundation.core.data.DbConfig;
import io.soffa.foundation.core.openapi.OpenAPIDesc;
import io.soffa.foundation.core.security.model.SecurityConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppConfig {

    private boolean sysLogs = true;
    private String name;
    private String pkg;
    private String version;
    private DbConfig db;
    private OpenAPIDesc openapi;
    private SecurityConfig security;

    private boolean configured;

    public AppConfig(String name) {
        this.name = name;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasDataSources() {
        return db != null && CollectionUtil.isNotEmpty(db.getDatasources());
    }

    public void configure() {
        if (configured) {
            return;
        }
        if (db != null && TextUtil.isNotEmpty(db.getTablesPrefix())) {
            String value = TextUtil.trimToEmpty(db.getTablesPrefix())
                .replaceAll("[^a-zA-Z0-9]", "_")
                .replaceAll("_+$", "_").trim();

            if (!value.endsWith("_")) {
                value += "_";
            }
            db.setTablesPrefix(value);
        }
        DefaultRequestContext.setServiceName(name);
        configured = true;
    }

}
