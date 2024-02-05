package com.rightpair.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Collections;

@OpenAPIDefinition(
        info = @Info(
                title = "Auth JWT - REST API Docs",
                description = "spring-security-auth-example"
        )
)
@Configuration
public class SwaggerConfig {
    public static final String JWT_SCHEME = "bearer";
    public static final String JWT_SCHEME_AUTH = "bearerAuth";

    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme(JWT_SCHEME).bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name(HttpHeaders.AUTHORIZATION);
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(JWT_SCHEME_AUTH, securityScheme))
                .security(Collections.singletonList(new SecurityRequirement().addList(JWT_SCHEME_AUTH)));
    }
}
