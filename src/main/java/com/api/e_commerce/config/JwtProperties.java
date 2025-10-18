package com.api.e_commerce.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    /**
     * Secret key for JWT token generation
     */
    private String secret = "mySecretKeyForJWTTokenGenerationThatShouldBeLongEnoughForSecurity";
    
    /**
     * JWT token expiration time in milliseconds (default 24 hours)
     */
    private Long expiration = 86400000L;
}