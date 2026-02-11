package com.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking Transaction System API")
                        .version("1.0.0")
                        .description("Mini Core Banking backend APIs for account and transaction management")
                        .contact(new Contact().name("Banking Platform Team").email("engineering@banking.local"))
                        .license(new License().name("Internal Use").url("https://example.com")));
    }
}
