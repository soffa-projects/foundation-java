package io.soffa.foundation.service.core.config;

import io.soffa.foundation.service.core.config.model.AppCorsProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsWebConfig implements WebMvcConfigurer {

    private final AppCorsProperties props;

    public CorsWebConfig(AppCorsProperties props) {
        this.props = props;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var reg = registry.addMapping("/**");

        if (!props.getAllowedOrigins().isEmpty()) {
            reg.allowedOrigins(props.getAllowedOrigins().toArray(String[]::new));
        }

        if (!props.getAllowedOriginPatterns().isEmpty()) {
            reg.allowedOriginPatterns(props.getAllowedOriginPatterns().toArray(String[]::new));
        }

        if (!props.getAllowedMethods().isEmpty()) {
            reg.allowedMethods(props.getAllowedMethods().toArray(String[]::new));
        }

        if (!props.getAllowedHeaders().isEmpty()) {
            reg.allowedHeaders(props.getAllowedHeaders().toArray(String[]::new));
        }

        if (!props.getExposedHeaders().isEmpty()) {
            reg.exposedHeaders(props.getExposedHeaders().toArray(String[]::new));
        }

        if (props.getAllowCredentials() != null) {
            reg.allowCredentials(props.getAllowCredentials());
        }

        if (props.getMaxAge() != null) {
            reg.maxAge(props.getMaxAge());
        }
    }

}
