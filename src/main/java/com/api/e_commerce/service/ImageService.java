package com.api.e_commerce.service;

import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

/**
 * Servicio para manejar URLs de imágenes y evitar problemas de CORB
 */
@Service
public class ImageService {

    private static final String PROXY_ENDPOINT = "/api/proxy/image";
    
    // Patrones para detectar URLs de imágenes externas
    private static final Pattern EXTERNAL_IMAGE_PATTERN = Pattern.compile(
        "^https?://(images\\.unsplash\\.com|plus\\.unsplash\\.com|cdn\\.|.*\\.(jpg|jpeg|png|gif|webp))", 
        Pattern.CASE_INSENSITIVE
    );

    /**
     * Convierte una URL de imagen externa a una URL de proxy para evitar CORB
     */
    public String getProxiedImageUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return null;
        }

        // Si es una URL local, no necesita proxy
        if (originalUrl.startsWith("/") || originalUrl.startsWith("data:") || 
            originalUrl.contains("localhost") || originalUrl.contains("127.0.0.1")) {
            return originalUrl;
        }

        // Si es una URL externa de imagen, usar el proxy
        if (EXTERNAL_IMAGE_PATTERN.matcher(originalUrl).find()) {
            return PROXY_ENDPOINT + "?url=" + encodeUrl(originalUrl);
        }

        // Para otros casos, devolver la URL original
        return originalUrl;
    }

    /**
     * Convierte URLs de imágenes en un texto/JSON para usar el proxy
     */
    public String processImageUrls(String content) {
        if (content == null) {
            return null;
        }

        // Buscar y reemplazar URLs de imágenes en el contenido
        return content.replaceAll(
            "(https?://(?:images\\.unsplash\\.com|plus\\.unsplash\\.com|cdn\\.|[^\\s]+\\.(?:jpg|jpeg|png|gif|webp)))",
            PROXY_ENDPOINT + "?url=$1"
        );
    }

    /**
     * Verificar si una URL necesita proxy
     */
    public boolean needsProxy(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        return EXTERNAL_IMAGE_PATTERN.matcher(url).find() && 
               !url.contains("localhost") && 
               !url.contains("127.0.0.1");
    }

    /**
     * Codificar URL para uso seguro como parámetro
     */
    private String encodeUrl(String url) {
        try {
            return java.net.URLEncoder.encode(url, "UTF-8");
        } catch (Exception e) {
            return url; // Fallback en caso de error
        }
    }

    /**
     * Obtener URL de imagen con fallback
     */
    public String getImageUrlWithFallback(String originalUrl, String fallbackUrl) {
        String proxiedUrl = getProxiedImageUrl(originalUrl);
        
        if (proxiedUrl != null) {
            return proxiedUrl;
        }
        
        return getProxiedImageUrl(fallbackUrl);
    }

    /**
     * Generar URLs de placeholder para desarrollo
     */
    public String getPlaceholderImageUrl(int width, int height, String text) {
        String baseUrl = "https://via.placeholder.com/" + width + "x" + height;
        if (text != null && !text.trim().isEmpty()) {
            baseUrl += "?text=" + encodeUrl(text);
        }
        return getProxiedImageUrl(baseUrl);
    }
}