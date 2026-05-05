package com.cati.tcc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       
        registry.addResourceHandler("/imagens/**")
        		.addResourceLocations("file:D:/programacao/TCC/fotos-tcc-cati/")
        		.setCachePeriod(0);
    }

}
