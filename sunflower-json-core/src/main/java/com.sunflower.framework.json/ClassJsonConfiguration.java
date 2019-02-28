package com.sunflower.framework.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;

/**
 * @author sunflower
 */
@Configuration
public class ClassJsonConfiguration {

	public ClassJsonConfiguration() {
	}

	@Bean
	@Primary
	public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter(
			ObjectMapper objectMapper) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(
				objectMapper);
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,
				new MediaType("application", "*+json"), MediaType.TEXT_PLAIN,
				MediaType.TEXT_HTML));
		return converter;
	}

}