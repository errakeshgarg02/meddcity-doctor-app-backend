package com.arg.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@EnableJpaAuditing
@Configuration
public class BeanConfig {
	
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
