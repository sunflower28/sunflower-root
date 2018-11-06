package com.sunflower.framework.http;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class RestTemplateConfig {

	public RestTemplateConfig() {
	}

	@LoadBalanced
	@Bean
	public RestTemplateUtil loadBalanced(ClientHttpRequestFactory requestFactory,
			MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
		RestTemplateUtil restTemplateUtil = new RestTemplateUtil();
		restTemplateUtil.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		restTemplateUtil.setRequestFactory(requestFactory);
		return restTemplateUtil;
	}

	@Bean
	@Primary
	public RestTemplateUtil restTemplateUtil(ClientHttpRequestFactory requestFactory,
			MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
		RestTemplateUtil restTemplateUtil = new RestTemplateUtil();
		restTemplateUtil.getMessageConverters().add(mappingJackson2HttpMessageConverter);
		restTemplateUtil.setRequestFactory(requestFactory);
		return restTemplateUtil;
	}

	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(3000);
		return factory;
	}

}
