package com.lcwd.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;


/*@OpenAPIDefinition(
        info = @Info(
                title = "Claims Service",
                version = "1.0",
                description = "Claims Information"
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)*/
@Configuration
public class SwaggerConfig {

	String schemeName = "bearerAuth";
	String bearerFormat = "JWT";
	String scheme = "bearer";

	@Bean
	public OpenAPI customiseOpenApi() {
		OpenAPI api = new OpenAPI();
		api.addSecurityItem(new SecurityRequirement()
				.addList(schemeName)
				).components(new Components()
						.addSecuritySchemes(schemeName, new SecurityScheme()
								.name(schemeName)
								.type(Type.HTTP)
								.bearerFormat(bearerFormat)
								.scheme(scheme)));

		api.info(new Info().
				title("Electronic Store API")
				.description("Spring Boot Electronic Store Application")
				.version("1.0.0v")
				.contact(new Contact().name("Pradeep").email("pradeep.huded@gmail.com").url("learnspringcode.com"))
				.license(new License().name("Apache")))
		.externalDocs(new ExternalDocumentation().url("learnspringcode.com").description("this is external url"));


		return api;
	}
}
