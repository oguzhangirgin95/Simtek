package com.example.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI simtekOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Simtek API")
                .version("v1"));
    }
}
