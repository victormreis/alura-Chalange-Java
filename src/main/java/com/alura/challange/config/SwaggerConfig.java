package com.alura.challange.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Challeng Alura API")
                        .description("Documentation of Alura Challange API")
                        .version("v1.0.0")
                        .termsOfService("https://www.victor.ca")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Developer helpCenter")
                                .url("https://www.victor.ca/suport")
                                .email("suport@mycompany.ca"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .components(new Components()
                );
    }
}