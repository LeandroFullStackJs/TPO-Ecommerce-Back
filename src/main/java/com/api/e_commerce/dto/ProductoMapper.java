package com.api.e_commerce.dto;

import com.api.e_commerce.model.Producto;
import com.api.e_commerce.model.Categoria;
import com.api.e_commerce.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre Producto y ProductoDTO
 * Incluye compatibilidad con aliases del frontend
 */
@Component
public class ProductoMapper {
    
    @Autowired
    private ImageService imageService;
    
    /**
     * Convierte un Producto a ProductoDTO con aliases para compatibilidad con frontend
     */
    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) return null;
        
        ProductoDTO dto = new ProductoDTO();
        
        // Campos principales
        dto.setId(producto.getId());
        dto.setNombreObra(producto.getNombreObra());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagen(imageService.getProxiedImageUrl(producto.getImagen()));
        dto.setActivo(producto.getActivo());
        dto.setDestacado(producto.getDestacado());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        
        // Campos de arte
        dto.setArtista(producto.getArtista());
        dto.setTecnica(producto.getTecnica());
        dto.setDimensiones(producto.getDimensiones());
        dto.setAnio(producto.getAnio());
        dto.setEstilo(producto.getEstilo());
        
        // IDs de relaciones
        if (producto.getArtistaEntity() != null) {
            dto.setArtistaId(producto.getArtistaEntity().getId());
            dto.setArtistaBiografia(producto.getArtistaEntity().getBiografia());
            dto.setArtistaImagenPerfil(producto.getArtistaEntity().getImagenPerfil());
        }
        
        if (producto.getUsuarioCreador() != null) {
            dto.setUsuarioId(producto.getUsuarioCreador().getId());
        }
        
        // Categorías
        if (producto.getCategorias() != null) {
            dto.setCategorias(producto.getCategorias().stream()
                .map(Categoria::getNombre)
                .collect(Collectors.toList()));
            dto.setCategoriaIds(producto.getCategorias().stream()
                .map(Categoria::getId)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * Convierte un ProductoCreateDTO a Producto
     */
    public Producto fromCreateDTO(ProductoCreateDTO createDTO) {
        if (createDTO == null) return null;
        
        Producto producto = new Producto();
        producto.setNombreObra(createDTO.getNombreObra());
        producto.setDescripcion(createDTO.getDescripcion() != null ? createDTO.getDescripcion() : "Descripción no proporcionada");
        producto.setPrecio(createDTO.getPrecio());
        producto.setStock(createDTO.getStock());
        producto.setImagen(createDTO.getImagen() != null ? createDTO.getImagen() : "https://via.placeholder.com/400x500?text=Sin+Imagen");
        producto.setArtista(createDTO.getArtista() != null ? createDTO.getArtista() : "Artista desconocido");
        producto.setTecnica(createDTO.getTecnica() != null ? createDTO.getTecnica() : "Técnica no especificada");
        producto.setDimensiones(createDTO.getDimensiones() != null ? createDTO.getDimensiones() : "Dimensiones no especificadas");
        producto.setAnio(createDTO.getAnio() != null ? createDTO.getAnio() : java.time.Year.now().getValue());
        producto.setEstilo(createDTO.getEstilo() != null ? createDTO.getEstilo() : "Estilo no especificado");
        producto.setDestacado(createDTO.getDestacado() != null ? createDTO.getDestacado() : false);
        producto.setActivo(createDTO.getActivo() != null ? createDTO.getActivo() : true);
        
        return producto;
    }
    
    /**
     * Actualiza un Producto existente con datos de ProductoUpdateDTO
     */
    public void updateFromDTO(Producto producto, ProductoUpdateDTO updateDTO) {
        if (producto == null || updateDTO == null) return;
        
        if (updateDTO.getNombre() != null) producto.setNombreObra(updateDTO.getNombre());
        if (updateDTO.getDescripcion() != null) producto.setDescripcion(updateDTO.getDescripcion());
        if (updateDTO.getPrecio() != null) producto.setPrecio(updateDTO.getPrecio());
        if (updateDTO.getStock() != null) producto.setStock(updateDTO.getStock());
        if (updateDTO.getImagen() != null) producto.setImagen(updateDTO.getImagen());
        if (updateDTO.getArtista() != null) producto.setArtista(updateDTO.getArtista());
        if (updateDTO.getTecnica() != null) producto.setTecnica(updateDTO.getTecnica());
        if (updateDTO.getDimensiones() != null) producto.setDimensiones(updateDTO.getDimensiones());
        if (updateDTO.getAnio() != null) producto.setAnio(updateDTO.getAnio());
        if (updateDTO.getEstilo() != null) producto.setEstilo(updateDTO.getEstilo());
        if (updateDTO.getDestacado() != null) producto.setDestacado(updateDTO.getDestacado());
        if (updateDTO.getActivo() != null) producto.setActivo(updateDTO.getActivo());
    }
}