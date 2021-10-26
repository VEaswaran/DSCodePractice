package com.datasite.practice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate()
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>()
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter()
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL))
        messageConverters.add(converter)
        restTemplate.setMessageConverters(messageConverters)
        restTemplate
    }


}
