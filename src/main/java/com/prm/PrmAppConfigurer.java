package com.prm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@EnableJpaRepositories
@Import(RepositoryRestMvcConfiguration.class)
@EnableAutoConfiguration
@ComponentScan
@EnableTransactionManagement
@EnableSpringDataWebSupport
public class PrmAppConfigurer extends WebMvcConfigurerAdapter {
	@Autowired
	@Qualifier("halJacksonHttpMessageConverter")
	private MappingJackson2HttpMessageConverter halJacksonHttpMessageConverter;
	
	@Autowired
	@Qualifier("jacksonHttpMessageConverter")
	private MappingJackson2HttpMessageConverter jacksonHttpMessageConverter;

	public static void main(String[] args) {
		SpringApplication.run(PrmAppConfigurer.class, args);
	}	
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		jacksonHttpMessageConverter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		converters.add(jacksonHttpMessageConverter);
		halJacksonHttpMessageConverter.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		converters.add(halJacksonHttpMessageConverter);
	}

}
