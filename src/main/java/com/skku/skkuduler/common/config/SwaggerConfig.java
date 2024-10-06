package com.skku.skkuduler.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${spring.server.base.url}")
    private String baseUrl;
    @Bean
    public OpenAPI customOpenApi() {
        Server server = new Server();
        server.setUrl(baseUrl);
        return new OpenAPI().addServersItem(server);
    }
}
