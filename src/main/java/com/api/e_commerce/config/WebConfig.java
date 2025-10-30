package com.api.e_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * Configuración adicional para Web MVC y RestTemplate
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(
                    "http://localhost:3000",    // React development server
                    "http://localhost:5173",    // Vite development server  
                    "http://localhost:4173",    // Vite preview server
                    "http://127.0.0.1:3000",
                    "http://127.0.0.1:5173",
                    "http://127.0.0.1:4173",
                    "https://*.vercel.app",     // Vercel deployments
                    "https://*.netlify.app"     // Netlify deployments
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Bean de RestTemplate configurado para el proxy de imágenes
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Configurar factory para timeouts
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 10 segundos
        factory.setReadTimeout(30000);    // 30 segundos
        
        restTemplate.setRequestFactory(factory);
        
        return restTemplate;
    }
}