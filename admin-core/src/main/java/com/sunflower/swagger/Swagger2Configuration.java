package com.sunflower.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author sunflower
 */
@Configuration
@EnableSwagger2
@ConditionalOnClass({ BeanValidatorPluginsConfiguration.class,
		Swagger2DocumentationConfiguration.class })
@Profile({ "uat", "dev" })
@Import({ BeanValidatorPluginsConfiguration.class })
public class Swagger2Configuration {

	public Swagger2Configuration() {
	}

	@Bean
	public Docket buildDocket() {
		return (new Docket(DocumentationType.SWAGGER_2)).select()
				.apis(RequestHandlerSelectors.any()).build()
				.directModelSubstitute(LocalDate.class, Date.class)
				.directModelSubstitute(LocalDateTime.class, java.util.Date.class);
	}

}
