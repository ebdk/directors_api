package com.directors_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        // Add a ByteArrayHttpMessageConverter for binary responses
        messageConverters.add(new ByteArrayHttpMessageConverter());

        // Add a Jackson converter for JSON responses
        messageConverters.add(new MappingJackson2HttpMessageConverter());

        // Add StringHttpMessageConverter to handle text responses
        messageConverters.add(new StringHttpMessageConverter());

        RestTemplate restTemplate = new RestTemplate(messageConverters);

        // Add the Accept header to request JSON responses
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Accept", "application/json");
            return execution.execute(request, body);
        });

        return restTemplate;
    }

}

