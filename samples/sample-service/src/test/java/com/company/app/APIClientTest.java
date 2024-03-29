package com.company.app;

import com.company.app.core.PingResponse;
import com.company.app.gateways.API;
import io.soffa.foundation.core.RequestContext;
import io.soffa.foundation.core.RestClient;
import io.soffa.foundation.core.context.DefaultRequestContext;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APIClientTest  {

    @LocalServerPort
    private int port;

    @SneakyThrows
    @Test
    public void testAPIClient() {
        API client = RestClient.newInstance(API.class, "http://localhost:" + port);
        RequestContext context = DefaultRequestContext.create("T1");
        PingResponse response = client.ping(context);
        assertEquals("PONG", response.getValue());
        assertEquals("Hello", client.echo("Hello", context));
    }


}
