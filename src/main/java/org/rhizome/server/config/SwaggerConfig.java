package org.rhizome.server.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(
        info =
                @io.swagger.v3.oas.annotations.info.Info(
                        title = "Rhizome Project API",
                        description = "Rhizome Project API 명세서",
                        version = "v1.0.0"))
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("/");

        return new OpenAPI().servers(List.of(server));
    }
}
