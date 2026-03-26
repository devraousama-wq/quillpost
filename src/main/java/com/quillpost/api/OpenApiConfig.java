package com.quillpost.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    io.swagger.v3.oas.models.OpenAPI quillpostOpenApi() {
        return new io.swagger.v3.oas.models.OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("Quillpost API")
                .version("0.1")
                .description("Public REST API for posts, media, and taxonomies"));
    }
}
