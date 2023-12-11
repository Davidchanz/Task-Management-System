package com.effectivemobile.TaskManagementSystem.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("com.effectivemobile")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI metaInfo() {
        return new OpenAPI()
                .info(new Info().title("Backend API For the TaskManagementSystem")
                        .description("Backend API For the TaskManagementSystem")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Security Description")
                        .url("https://github.com/Davidchanz/Task-Management-System"));
    }

}
