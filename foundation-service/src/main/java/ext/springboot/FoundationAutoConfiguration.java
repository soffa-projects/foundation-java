package ext.springboot;

import io.soffa.foundation.service.core.config.model.AppCorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableScheduling
@Configuration
@EnableConfigurationProperties(AppCorsProperties.class)
@ComponentScan({"io.soffa.foundation.service.core"})
//@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class FoundationAutoConfiguration {

}
