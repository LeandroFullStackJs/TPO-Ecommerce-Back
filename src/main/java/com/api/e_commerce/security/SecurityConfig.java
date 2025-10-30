package com.api.e_commerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;
    
    @Autowired
    @Lazy
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints de utilidad públicos
                .requestMatchers("/api/health", "/api/info").permitAll()
                // Proxy de imágenes público (evita CORB)
                .requestMatchers("/api/proxy/**").permitAll()
                // Auth endpoints
                .requestMatchers("/api/auth/**").permitAll()
                // Demo endpoints for testing exceptions
                .requestMatchers("/api/demo/**").permitAll() 
                // Productos - GET operations public
                .requestMatchers("/api/productos", "/api/productos/{id}", "/api/productos/buscar", "/api/productos/destacados", "/api/productos/categoria/{categoriaId}").permitAll() 
                // Categories public
                .requestMatchers("/api/categorias", "/api/categorias/{id}").permitAll() 
                // Artistas - GET operations public
                .requestMatchers("/api/artistas", "/api/artistas/{id}", "/api/artistas/buscar").permitAll() 
                // H2 Console (for development)
                .requestMatchers("/h2-console/**").permitAll()
                // OPTIONS requests (for CORS preflight)
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        // Para H2 Console - usando el método actualizado
        http.headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin())
        );
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}