package com.sunflower.framework.swagger;

import com.sunflower.member.MemberUserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@Profile({ "uat", "dev" })
public class Swagger2Configuration {

	public Swagger2Configuration() {
	}

	@Bean
	public Docket buildDocket() {
		List<Parameter> operationParameters = new ArrayList();
		operationParameters.add((new ParameterBuilder()).name("token").description("令牌")
				.modelRef(new ModelRef("string")).parameterType("header").required(false)
				.build());
		return (new Docket(DocumentationType.SWAGGER_2)).select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build()
				.globalOperationParameters(operationParameters)
				.ignoredParameterTypes(new Class[] { MemberUserProfile.class })
				.directModelSubstitute(LocalDate.class, Date.class)
				.directModelSubstitute(LocalDateTime.class, java.util.Date.class);
	}

}
