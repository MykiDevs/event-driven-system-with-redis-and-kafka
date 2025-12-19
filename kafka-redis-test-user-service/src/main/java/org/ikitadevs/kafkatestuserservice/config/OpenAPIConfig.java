package org.ikitadevs.kafkatestuserservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        return new OpenAPI()
                .info(new Info()
                .title("User service API")
                .description("Simple User Service API")
                .version("1.0")
                .contact(new Contact().email("example@example.com").name("Nick").url("https://example.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Server for developing")
                ));
    }
}
